package com.app.Music_Web.Application.Ports.In.Playlist;

import java.util.List;

import com.app.Music_Web.Application.DTO.PlaylistDTO;
import com.app.Music_Web.Application.DTO.PlaylistSongDTO;

public interface FindPlaylistService {
    List<PlaylistDTO> findPlaylistByUserId(String email);
    List<PlaylistSongDTO> findSongIdsByPlaylistId(Long playlistId);
}
