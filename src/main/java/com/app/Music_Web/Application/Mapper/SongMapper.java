package com.app.Music_Web.Application.Mapper;

import java.util.stream.Collectors;

import com.app.Music_Web.Application.DTO.GenreDTO;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Domain.Entities.Song;

public class SongMapper {
    public static SongDTO toDTO(Song song) {
        return SongDTO.builder()
            .songId(song.getSongId())
            .title(song.getTitle().getTitle())
            .artist(song.getArtist().getArtist())
            .songImage(song.getSongImage())
            .fileSongId(song.getFileSongId())
            .downloadable(song.isDownloadable())
            .genres(song.getSongGenres().stream()
                       .map(songGenre -> GenreDTO.builder()
                            .genreId(songGenre.getGenre().getGenreId())
                            .genreName(songGenre.getGenre().getGenreName().getGenreName())
                            .build())
                        .collect(Collectors.toList()))
            .build();
    }
}

