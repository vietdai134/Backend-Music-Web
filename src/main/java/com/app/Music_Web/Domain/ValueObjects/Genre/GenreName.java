package com.app.Music_Web.Domain.ValueObjects.Genre;

import com.app.Music_Web.Domain.Exceptions.GenreException.GenreInvalidException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class GenreName {
    @Column(name = "genre_name",nullable = false,length = 255)
    private String genreName;

    public GenreName(String genreName) {
        if(genreName == null || genreName.isEmpty()){
            throw new GenreInvalidException("Genre name is required");
        }
        if (genreName.length()>255){
            throw new GenreInvalidException("Genre name is too long");
        }
        this.genreName = genreName.trim();
    }
}
