package com.app.Music_Web.Application.Ports.In.Playlist;

import java.util.List;

import com.app.Music_Web.Application.DTO.PlaylistDTO;

public interface SavePlaylistService {
    PlaylistDTO createPlaylist(String playListName,  String email);
    void saveSongToPlaylist(Long songId, Long playlistId);
    void saveMultipleSongsToPlaylist(List<Long> songIds, Long playlistId);
}
