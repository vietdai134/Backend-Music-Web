package com.app.Music_Web.Application.Ports.In.Playlist;

public interface DeletePlaylistService {
    void deletePlaylist(Long playlistId );  
    void deleteSongFromPlaylist(Long songId, Long playlistId);  
}
