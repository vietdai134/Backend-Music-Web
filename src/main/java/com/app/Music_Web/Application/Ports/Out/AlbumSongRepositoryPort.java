package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.AlbumSong;

public interface AlbumSongRepositoryPort {
    AlbumSong save(AlbumSong albumSong);
}
