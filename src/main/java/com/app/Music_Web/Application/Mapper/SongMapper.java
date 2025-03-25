package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Domain.Entities.Song;

public class SongMapper {
    public static SongDTO toDTO(Song song) {
        return SongDTO.builder()
            .id(song.getSongId())
            .title(song.getTitle().getTitle())
            .artist(song.getArtist().getArtist())
            .song_image(song.getSongImage())
            .fileSongId(song.getFileSongId())
            .downloadable(song.isDownloadable())
            .build();
    }
}

