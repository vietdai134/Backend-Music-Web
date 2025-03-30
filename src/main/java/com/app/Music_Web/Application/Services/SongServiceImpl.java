package com.app.Music_Web.Application.Services;

import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;
import com.app.Music_Web.Application.Ports.In.Song.UpdateSongService;
import com.app.Music_Web.Application.Ports.Out.GenreRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Domain.Entities.Genre;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.SongGenre;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;
import com.app.Music_Web.Domain.ValueObjects.Song.SongArtist;
import com.app.Music_Web.Domain.ValueObjects.Song.SongTitle;
import com.app.Music_Web.Application.DTO.GenreDTO;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.Mapper.GenreMapper;
import com.app.Music_Web.Application.Mapper.SongMapper;
import com.app.Music_Web.Application.Ports.In.Cloudinary.CloudinaryService;
import com.app.Music_Web.Application.Ports.In.Song.DeleteSongService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class SongServiceImpl implements SaveSongService, FindSongService,DeleteSongService, UpdateSongService {
    private final SongRepositoryPort songRepositoryPort;
    private final GenreRepositoryPort genreRepositoryPort;
    private final CloudinaryService cloudinaryService;
    public SongServiceImpl(SongRepositoryPort songRepositoryPort,
                            GenreRepositoryPort genreRepositoryPort,
                            CloudinaryService cloudinaryService) {
        this.songRepositoryPort = songRepositoryPort;
        this.genreRepositoryPort= genreRepositoryPort;
        this.cloudinaryService=cloudinaryService;
    }

    @Override
    public Page<SongDTO> findAll(Pageable pageable) {
        Page<Song> songs = songRepositoryPort.findAll(pageable);
        return songs.map(SongMapper::toDTO);
    }

    @Override
    public Page<SongDTO> findAllWithStatus(ApprovalStatus status, Pageable pageable) {
        Page<Object[]> result = songRepositoryPort.findAllWithStatus(status, pageable);
        List<Long> songIds=result.stream()
                                .map(row->(Long) row[0])
                                .collect(Collectors.toList());

        List<Genre> genres= genreRepositoryPort.findGenresBySongId(songIds);
        // List<GenreDTO> genreDTOs= genres.stream()
        //                                 .map(GenreMapper::toDTO)
        //                                 .collect(Collectors.toList());

        Map<Long, List<GenreDTO>> genresBySongId = genres.stream()
            .flatMap(genre -> genre.getSongGenres().stream()
                .map(sg -> new AbstractMap.SimpleEntry<>(sg.getSong().getSongId(), GenreMapper.toDTO(genre)))
            )
            .filter(entry -> entry.getKey() != null && songIds.contains(entry.getKey())) // Đảm bảo songId hợp lệ
            .collect(Collectors.groupingBy(Map.Entry::getKey, 
                Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        
        return result.map(row -> {
            Long songId = (Long) row[0];
            return SongDTO.builder()
                .songId((Long) row[0])
                .title((String) row[1])
                .artist((String) row[2])
                .songImage((String) row[3])
                .fileSongId((String) row[4])
                .downloadable((Boolean) row[5])
                .approvedDate((Date) row[6])
                .userName((String) row[7])
                .genres(genresBySongId.getOrDefault(
                    songId, 
                    Collections.emptyList()))
                .build();
        });
        
    }

    @Override
    public SongDTO saveSong(String title, String artist, MultipartFile songImage, 
                    String fileSongId,List<String> genreNames ,
                    boolean downloadable) throws Exception {
        Song song = Song.builder()
                        .title(new SongTitle(title))
                        .artist(new SongArtist(artist))
                        // .songImage(song_image)
                        .fileSongId(fileSongId)
                        .downloadable(downloadable)
                        .build();

        // Khởi tạo songGenres nếu null
        if (song.getSongGenres() == null) {
            song.setSongGenres(new ArrayList<>());
        }

         // Tạo CompletableFuture cho việc upload ảnh
        CompletableFuture<String> songImgFuture = (songImage != null && !songImage.isEmpty())
        ? CompletableFuture.supplyAsync(() -> {
            try {
                String folder = "songs/songImages/" + fileSongId;
                return cloudinaryService.uploadAvatar(songImage, folder);
            } catch (IOException e) {
                throw new RuntimeException("Upload failed", e);
            }
        })
        : CompletableFuture.completedFuture("https://res.cloudinary.com/dutcbjnyb/image/upload/v1742989033/songs/songImages/default/a669uahfutmmki9crcbt.jpg");
        
        List<Genre> genres= genreRepositoryPort.findByGenreNameIn(genreNames);
        System.out.println("genres: " + genres);
        List<SongGenre> songGenres= genres.stream()
                        .map(genre -> SongGenre.builder()
                        .song(song)
                        .genre(genre)
                        .build())
                        .collect(Collectors.toList());

        song.getSongGenres().addAll(songGenres);

        String songImgUrl = songImgFuture.get(); // Chặn ở đây để lấy URL
        if (songImgUrl != null) {
            song.setSongImage(songImgUrl);
        }
        Song saveSong = songRepositoryPort.save(song);
        return SongMapper.toDTO(saveSong);
    }

    @Override
    public void deleteSong(Long songId) {
        Song song = songRepositoryPort.findBySongId(songId);
        if(song==null){
            throw new RuntimeException("song not found");
        }
        songRepositoryPort.delete(song);
    }

    @Override
    public SongDTO findBySongTitle(String songTitle) {
        Song song=songRepositoryPort.findByTitle_Title(songTitle);
        if(song==null){
            throw new RuntimeException("Song not found");
        }
        return SongMapper.toDTO(song);
    }

    @Override
    public SongDTO findBySongArtist(String songArtist) {
        Song song=songRepositoryPort.findByArtist_Artist(songArtist);
        if(song==null){
            throw new RuntimeException("Song not found");
        }
        return SongMapper.toDTO(song);
    }

    @Override
    public Long findByFileSongId(String fileSongId) {
        Song song = songRepositoryPort.findByFileSongId(fileSongId);
        return song.getSongId();
    }

    @Override
    public SongDTO findBySongId(Long songId) {
        Song song = songRepositoryPort.findBySongId(songId);
        
        return SongMapper.toDTO(song);
    }

    @Override
    public SongDTO updateSong(Long songId, String title, String artist,String fileSongId,
                            MultipartFile songImg, List<String> genreNames,
                            boolean downloadable) throws Exception {
        Song song = songRepositoryPort.findBySongId(songId);

        song.setTitle(new SongTitle(title));
        song.setArtist(new SongArtist(artist));
        song.setDownloadable(downloadable);

        CompletableFuture<String> imgFuture = (songImg != null && !songImg.isEmpty())
        ? CompletableFuture.supplyAsync(() -> {
            try {
                // Xóa avatar cũ nếu có
                if (song.getSongImage() != null && !song.getSongImage().isEmpty()) {
                    String oldPublicId = cloudinaryService.extractPublicIdFromUrl(song.getSongImage());
                    if (oldPublicId != null) {
                        cloudinaryService.deleteAvatar(oldPublicId);
                    }
                }
                String folder = "songs/songImages/" + fileSongId;
                return cloudinaryService.uploadAvatar(songImg, folder);
            } catch (IOException e) {
                throw new RuntimeException("Upload failed", e);
            }
        })
        : CompletableFuture.completedFuture("https://res.cloudinary.com/dutcbjnyb/image/upload/v1742804273/users/userAvatars/default/sgsl4xyfmmsogrtozgmk.jpg");

        song.getSongGenres().clear();

        List<Genre> genres= genreRepositoryPort.findByGenreNameIn(genreNames);

        List<SongGenre> songGenres= genres.stream()
                    .map(genre -> SongGenre.builder()
                        .song(song)
                        .genre(genre)
                        .build())
                    .collect(Collectors.toList());
        
        song.getSongGenres().addAll(songGenres);

        String imgUrl = imgFuture.get();
        if(imgUrl != null){
            song.setSongImage(imgUrl);
        }
        
        Song updatedSong=songRepositoryPort.save(song);
        return SongMapper.toDTO(updatedSong);
    }

    @Override
    public Page<SongDTO> searchByTitleOrArtist(String keyword, ApprovalStatus status, Pageable pageable) {
        Page<Object[]> result= songRepositoryPort.searchByTitleOrArtist(keyword, status, pageable);
        List<Long> songIds= result.stream()
                                .map(row -> (Long) row[0])
                                .collect(Collectors.toList());
        List<Genre> genres=genreRepositoryPort.findGenresBySongId(songIds);

        Map<Long, List<GenreDTO>> genresBySongId = genres.stream()
            .flatMap(genre -> genre.getSongGenres().stream()
                .map(sg -> new AbstractMap.SimpleEntry<>(sg.getSong().getSongId(), GenreMapper.toDTO(genre)))
            )
            .filter(entry -> entry.getKey() != null && songIds.contains(entry.getKey())) // Đảm bảo songId hợp lệ
            .collect(Collectors.groupingBy(Map.Entry::getKey, 
                Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
                
        return result.map(row -> {
            Long songId = (Long) row[0];
            return SongDTO.builder()
                .songId((Long) row[0])
                .title((String) row[1])
                .artist((String) row[2])
                .songImage((String) row[3])
                .fileSongId((String) row[4])
                .downloadable((Boolean) row[5])
                .approvedDate((Date) row[6])
                .userName((String) row[7])
                .genres(genresBySongId.getOrDefault(
                    songId, 
                    Collections.emptyList()))
                .build();
        });
    }
 
}