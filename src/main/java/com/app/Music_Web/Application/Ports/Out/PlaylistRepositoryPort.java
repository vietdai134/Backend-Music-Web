package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.Playlist;

public interface PlaylistRepositoryPort {
    Playlist save(Playlist playlist);
}
