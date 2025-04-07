package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import com.app.Music_Web.Domain.Entities.Playlist;

public interface PlaylistRepositoryPort {
    Playlist save(Playlist playlist);
    List<Playlist> findByUser_UserId(Long userId);
    Playlist findByPlaylistId(Long playlistId);
    Playlist findByPlaylistName(String playlistName);
    boolean existsByPlaylistName(String playlistName);
}
