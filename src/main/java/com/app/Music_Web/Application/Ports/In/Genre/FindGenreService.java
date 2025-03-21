package com.app.Music_Web.Application.Ports.In.Genre;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Application.DTO.GenreDTO;

public interface FindGenreService {
    Page<GenreDTO> findAll (Pageable pageable);
    Page<GenreDTO> searchByGenreName(String keyword, Pageable pageable);

    // GenreDTO findByGenreName (String genreName);
    GenreDTO findByGenreId(Long genreId);

    
}
