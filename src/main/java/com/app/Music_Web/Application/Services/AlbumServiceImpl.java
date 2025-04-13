package com.app.Music_Web.Application.Services;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.Music_Web.Application.DTO.AlbumDTO;
import com.app.Music_Web.Application.DTO.AlbumSongDTO;
import com.app.Music_Web.Application.Mapper.AlbumMapper;
import com.app.Music_Web.Application.Ports.In.Album.DeleteAlbumService;
import com.app.Music_Web.Application.Ports.In.Album.FindAlbumService;
import com.app.Music_Web.Application.Ports.In.Album.SaveAlbumService;
import com.app.Music_Web.Application.Ports.In.Album.UpdateAlbumService;
import com.app.Music_Web.Application.Ports.In.Cloudinary.CloudinaryService;
import com.app.Music_Web.Application.Ports.Out.AlbumRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.AlbumSongRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.Album;
import com.app.Music_Web.Domain.Entities.AlbumSong;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.ValueObjects.Album.AlbumName;

@Service
public class AlbumServiceImpl implements SaveAlbumService, 
                                        FindAlbumService,
                                        DeleteAlbumService,
                                        UpdateAlbumService {
    private final AlbumRepositoryPort albumRepositoryPort;
    private final UserRepositoryPort  userRepositoryPort;
    private final SongRepositoryPort songRepositoryPort;
    private final AlbumSongRepositoryPort albumSongRepositoryPort;
    private final CloudinaryService cloudinaryService;
    public AlbumServiceImpl(
        AlbumRepositoryPort albumRepositoryPort,
        UserRepositoryPort userRepositoryPort,
        SongRepositoryPort songRepositoryPort,
        AlbumSongRepositoryPort albumSongRepositoryPort,
        CloudinaryService cloudinaryService
        ) {
        this.userRepositoryPort=userRepositoryPort;
        this.albumRepositoryPort=albumRepositoryPort;
        this.songRepositoryPort=songRepositoryPort;
        this.albumSongRepositoryPort=albumSongRepositoryPort;
        this.cloudinaryService=cloudinaryService;
    }
    @Override
    public AlbumDTO saveAlbum(String albumName, MultipartFile albumImage, Long userId) {
        User user = userRepositoryPort.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        Album album = Album.builder()
                            .albumName(new AlbumName(albumName))
                            // .albumImage(albumImage)
                            .createdDate(new Date())
                            .user(user)
                            .build();
        CompletableFuture<String> albumImageFuture = (albumImage != null && !albumImage.isEmpty())
        ? CompletableFuture.supplyAsync(() -> {
            try {
                String folder = "albums/albumImages/" + albumName+"-"+userId;
                return cloudinaryService.uploadAvatar(albumImage, folder);
            } catch (IOException e) {
                throw new RuntimeException("Upload failed", e);
            }
        })
        : CompletableFuture.completedFuture("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743621035/NoIMG_tmvbrh.jpg");
        
        String albumImgUrl = null;
        try {
            albumImgUrl = albumImageFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (albumImgUrl != null) {
            album.setAlbumImage(albumImgUrl);
        }
        Album albumed = albumRepositoryPort.save(album);
        return AlbumMapper.toAlbumDTO(albumed);
    }
    @Override
    public List<AlbumDTO> findAlbumByUserId(Long userId) {
        User user = userRepositoryPort.findById(userId)
            .orElseThrow(() -> new RuntimeException("Không có user"));
        List<Album> albums= albumRepositoryPort.findByUser_UserId(user.getUserId());
        return albums.stream()
                    .map(AlbumMapper::toAlbumDTO)
                    .toList();
    }
    @Override
    @Transactional
    public void updateAlbum(Long albumId, String albumName, 
                    MultipartFile albumImage, Long userId) {
        Album album = albumRepositoryPort.findByAlbumId(albumId);
        if (album == null) {
            throw new RuntimeException("Không tìm thấy album");
        }
        album.setAlbumName(new AlbumName(albumName));
        CompletableFuture<String> imgFuture = (albumImage != null && !albumImage.isEmpty())
        ? CompletableFuture.supplyAsync(() -> {
            try {
                // Xóa avatar cũ nếu có
                if (album.getAlbumImage() != null && !album.getAlbumImage().isEmpty()) {
                    String oldPublicId = cloudinaryService.extractPublicIdFromUrl(album.getAlbumImage());
                    if (oldPublicId != null) {
                        cloudinaryService.deleteAvatar(oldPublicId);
                    }
                }
                String folder = "albums/albumImages/" + albumName+"-"+userId;
                return cloudinaryService.uploadAvatar(albumImage, folder);
            } catch (IOException e) {
                throw new RuntimeException("Upload failed", e);
            }
        })
        : CompletableFuture.completedFuture(album.getAlbumImage());

        
        String imgUrl = null;
        try {
            imgUrl = imgFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error while fetching album image URL", e);
        }
        if(imgUrl != null){
            album.setAlbumImage(imgUrl);
        }
        albumRepositoryPort.save(album);
    }
    
    @Override
    @Transactional
    public void deleteAlbum(Long albumId) {
        albumRepositoryPort.deleteByAlbumId(albumId);
    }
    @Override
    @Transactional
    public void deleteSongFromAlbum(Long songId, Long albumId) {
        albumSongRepositoryPort.deleteBySong_SongIdAndAlbum_AlbumId(songId, albumId);
    }
    @Override
    public List<AlbumSongDTO> findSongIdsByAlbumId(Long albumId) {
        List<AlbumSong> albumSongs = albumSongRepositoryPort.findByAlbum_AlbumId(albumId);
        List<AlbumSongDTO> albumSongDTOs = albumSongs.stream()
                .map(albumSong -> AlbumSongDTO.builder()
                        .albumSongId(albumSong.getAlbumSongId())
                        .songId(albumSong.getSong().getSongId())
                        .albumId(albumSong.getAlbum().getAlbumId())
                        .build())
                        .toList();
        return albumSongDTOs;
    }
    @Override
    public void saveSongToAlbum(Long songId, Long albumId) {
        Song song = songRepositoryPort.findBySongId(songId);
        Album album = albumRepositoryPort.findByAlbumId(albumId);
        boolean exists = albumSongRepositoryPort.existsBySong_SongIdAndAlbum_AlbumId(songId, albumId);
        if (!exists) {
            AlbumSong albumSong= AlbumSong.builder()
                            .song(song)
                            .album(album)
                            .build();
            albumSongRepositoryPort.save(albumSong);
        }
        
    }
    @Override
    public void saveMultipleSongsToAlbum(List<Long> songIds, Long albumId) {
        Album album = albumRepositoryPort.findByAlbumId(albumId);
        for (Long songId: songIds){
            Song song = songRepositoryPort.findBySongId(songId);
            boolean exists = albumSongRepositoryPort.existsBySong_SongIdAndAlbum_AlbumId(songId, albumId);
            if (!exists) {
                AlbumSong albumSong= AlbumSong.builder()
                                .song(song)
                                .album(album)
                                .build();
                albumSongRepositoryPort.save(albumSong);
            }
        }
    }


    



    
}
