package com.app.Music_Web.Domain.ValueObjects.Album;

import com.app.Music_Web.Domain.Exceptions.AlbumException.AlbumInvalidException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class AlbumName {
    @Column(name = "album_name",nullable = false,length = 255)
    private String albumName;

    public AlbumName(String albumName) {
        if(albumName == null || albumName.isEmpty()){
            throw new AlbumInvalidException("Album name is required");
        }
        if (albumName.length()>255){
            throw new AlbumInvalidException("Album name is too long");
        }
        this.albumName = albumName.trim();
    }
}
