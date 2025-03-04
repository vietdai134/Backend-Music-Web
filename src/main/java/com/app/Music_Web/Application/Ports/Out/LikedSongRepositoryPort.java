package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.LikedSong;

public interface LikedSongRepositoryPort {
    LikedSong save(LikedSong likedSong);
}
