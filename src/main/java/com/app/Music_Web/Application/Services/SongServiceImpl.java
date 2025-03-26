package com.app.Music_Web.Application.Services;

import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;
import com.app.Music_Web.Application.Ports.Out.GenreRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Domain.Entities.Genre;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.SongGenre;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;
import com.app.Music_Web.Domain.ValueObjects.Song.SongArtist;
import com.app.Music_Web.Domain.ValueObjects.Song.SongTitle;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.Mapper.SongMapper;
import com.app.Music_Web.Application.Ports.In.Cloudinary.CloudinaryService;
import com.app.Music_Web.Application.Ports.In.Song.DeleteSongService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongService;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class SongServiceImpl implements SaveSongService, FindSongService,DeleteSongService {
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
        return result.map(row -> SongDTO.builder()
                .songId((Long) row[0])
                .title((String) row[1])
                .artist((String) row[2])
                .songImage((String) row[3])
                .fileSongId((String) row[4])
                .downloadable((Boolean) row[5])
                .approvedDate((Date) row[6])
                .userName((String) row[7])
                .build());
        
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
                String folder = "songs/songImages/" + artist+"-"+title;
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
 
}