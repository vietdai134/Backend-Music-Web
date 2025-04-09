package com.app.Music_Web.Application.Ports.In.LikedSong;

import java.util.List;

import com.app.Music_Web.Application.DTO.LikedSongDTO;

public interface FindLikedSongService {
    List<LikedSongDTO> findLikedSongsByUser(String email); // Find all liked songs for a specific user
}
