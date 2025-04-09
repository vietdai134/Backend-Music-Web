package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.LikedSongDTO;
import com.app.Music_Web.Domain.Entities.LikedSong;

public class LikedSongMapper {
    public static LikedSongDTO toLikedSongDTO(LikedSong likedSong) {
        return LikedSongDTO.builder()
                .likeId(likedSong.getLikeId())
                .songId(likedSong.getSong().getSongId())
                .userId(likedSong.getUser().getUserId())
                .likedDate(likedSong.getLikedDate())
                .build();
    }
}
