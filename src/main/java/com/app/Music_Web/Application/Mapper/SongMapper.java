package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.ValueObjects.Song.SongArtist;
import com.app.Music_Web.Domain.ValueObjects.Song.SongTitle;

public class SongMapper {
    public static SongDTO toDTO(Song song) {
        return SongDTO.builder()
            .id(song.getSongId())
            .title(song.getTitle().getTitle())
            .artist(song.getArtist().getArtist())
            .song_image(song.getSongImage())
            .source_url(song.getSourceUrl())
            .downloadable(song.isDownloadable())
            .build();
    }

    public static Song toEntity(SongDTO dto) {
        return Song.builder()
            .songId(dto.getId())
            .title(new SongTitle(dto.getTitle()))
            .artist(new SongArtist(dto.getArtist()))
            .songImage(dto.getSong_image())
            .sourceUrl(dto.getSource_url())
            .downloadable(dto.isDownloadable())
            .build();
    }
}

