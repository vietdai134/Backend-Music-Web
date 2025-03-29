package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Domain.Entities.Genre;

public interface GenreRepositoryPort {
    Page<Genre> findAll(Pageable pageable);
    Page<Genre> searchByGenreName(String keyword,Pageable pageable);
    List<Genre> findAll();
    Genre save (Genre genre);
    void delete(Genre genre);
    
    // Genre findByGenreName_GenreName(String genreName);
    Genre findByGenreId(Long genreId);

    List<Genre> findByGenreNameIn(List<String> genreNames);

    List<Genre> findGenresBySongId(List<Long> songId);

    List<Genre> findGenresBySongId(Long songId);
} 
