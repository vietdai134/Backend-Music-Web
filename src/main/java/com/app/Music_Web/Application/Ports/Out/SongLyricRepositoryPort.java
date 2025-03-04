package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.SongLyric;

public interface SongLyricRepositoryPort {
    SongLyric save(SongLyric songLyric);
}
