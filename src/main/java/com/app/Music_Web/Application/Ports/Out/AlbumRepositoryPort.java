package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.Album;

public interface AlbumRepositoryPort {
    Album save(Album album);
}
