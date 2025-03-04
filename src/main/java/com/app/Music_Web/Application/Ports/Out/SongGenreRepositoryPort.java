package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.SongGenre;

public interface SongGenreRepositoryPort {
    SongGenre save(SongGenre songGenre);
}
