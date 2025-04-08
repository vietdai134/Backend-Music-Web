package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import com.app.Music_Web.Domain.Entities.PlaylistSong;

public interface PlaylistSongRepositoryPort {
    PlaylistSong save(PlaylistSong playlistSong);
    void deleteBySong_SongIdAndPlaylist_PlaylistId(Long songId, Long playlistId);
    boolean existsBySong_SongIdAndPlaylist_PlaylistId(Long songId, Long playlistId);
    List<PlaylistSong> findByPlaylist_PlaylistId(Long playlistId);
}
