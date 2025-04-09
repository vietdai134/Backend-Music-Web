package com.app.Music_Web.Application.Ports.In.LikedSong;

public interface SaveLikedSongService {
    void saveLikedSong(Long songId, String email); // Save a liked song for a specific user
}
