package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import com.app.Music_Web.Domain.Entities.LikedSong;

public interface LikedSongRepositoryPort {
    LikedSong save(LikedSong likedSong);
    LikedSong findBySong_SongIdAndUser_UserId(Long songId, Long userId);
    void deleteBySong_SongIdAndUser_UserId(Long songId, Long userId);
    List<LikedSong> findAllByUser_UserId(Long userId);
}
