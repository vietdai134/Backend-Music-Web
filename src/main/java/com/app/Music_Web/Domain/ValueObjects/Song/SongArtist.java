package com.app.Music_Web.Domain.ValueObjects.Song;

import com.app.Music_Web.Domain.Exceptions.SongException.SongInvalidException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class SongArtist {
    @Column(name="artist",nullable = false,length = 255)
    private String artist;

    public SongArtist(String artist) {
        if(artist == null || artist.isEmpty()){
            throw new SongInvalidException("Song artist is required");
        }
        if (artist.length()>255){
            throw new SongInvalidException("Song artist is too long");
        }
        this.artist = artist.trim();
    }
}
