package com.app.Music_Web.Application.Ports.In.LikedSong;

public interface DeleteLikedSongService {
    void deleteLikedSong(Long songId, String email);
}
