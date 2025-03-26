package com.app.Music_Web.Application.Ports.In.Genre;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Application.DTO.GenreDTO;

public interface FindGenreService {
    Page<GenreDTO> findAll (Pageable pageable);
    Page<GenreDTO> searchByGenreName(String keyword, Pageable pageable);
    List<GenreDTO> findAll();
    // GenreDTO findByGenreName (String genreName);
    GenreDTO findByGenreId(Long genreId);

    
}
