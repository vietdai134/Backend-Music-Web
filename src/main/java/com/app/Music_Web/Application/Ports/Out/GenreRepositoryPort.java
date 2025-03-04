package com.app.Music_Web.Application.Ports.Out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Domain.Entities.Genre;

public interface GenreRepositoryPort {
    Page<Genre> findAll(Pageable pageable);
    Genre save (Genre genre);
    void delete(Genre genre);
    Genre findByGenreName_GenreName(String genreName);
    Genre findByGenreId(Long genreId);
} 
