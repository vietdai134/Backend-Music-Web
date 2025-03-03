package com.app.Music_Web.Domain.ValueObjects.Song;

import com.app.Music_Web.Domain.Exceptions.SongException.SongInvalidException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Getter
@NoArgsConstructor
@Embeddable
public class SongTitle {
    @Column(name = "title",nullable = false,length = 255)
    private String title;

    public SongTitle(String title) {
        if(title == null || title.isEmpty()){
            throw new SongInvalidException("Song title is required");
        }
        if (title.length()>255){
            throw new SongInvalidException("Song title is too long");
        }
        this.title = title.trim();
    }
}
