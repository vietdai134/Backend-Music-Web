package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.GenreDTO;
import com.app.Music_Web.Domain.Entities.Genre;
import com.app.Music_Web.Domain.ValueObjects.Genre.GenreName;

public class GenreMapper {
    public static GenreDTO toDTO(Genre genre) {
        return GenreDTO.builder()
            .genreId(genre.getGenreId())
            .genreName(genre.getGenreName().getGenreName())
            .build();
    }

    public static Genre toEntity(GenreDTO dto) {
        return Genre.builder()
            .genreId(dto.getGenreId())
            .genreName(new GenreName(dto.getGenreName()))
            .build();
    }
}
