package com.app.Music_Web.Application.Ports.In.Genre;

import com.app.Music_Web.Application.DTO.GenreDTO;

public interface SaveGenreService {
    GenreDTO saveGenre(String genreName);
}
