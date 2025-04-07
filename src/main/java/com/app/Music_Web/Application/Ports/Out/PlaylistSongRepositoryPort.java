package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.PlaylistSong;

public interface PlaylistSongRepositoryPort {
    PlaylistSong save(PlaylistSong playlistSong);
    boolean existsBySong_SongIdAndPlaylist_PlaylistId(Long songId, Long playlistId);
}
