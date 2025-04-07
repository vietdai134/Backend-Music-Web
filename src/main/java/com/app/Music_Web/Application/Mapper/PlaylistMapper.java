package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.PlaylistDTO;
import com.app.Music_Web.Domain.Entities.Playlist;

public class PlaylistMapper {
        public static PlaylistDTO toDTO(Playlist playlist) {
        return PlaylistDTO.builder()
            .playlistId(playlist.getPlaylistId())
            .playlistName(playlist.getPlaylistName())
            .createdBy(playlist.getUser().getUserId().toString())
            .createdDate(playlist.getCreatedDate())
            .build();
    }
}
