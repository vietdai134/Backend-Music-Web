package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.AlbumDTO;
import com.app.Music_Web.Domain.Entities.Album;

public class AlbumMapper {
    public static AlbumDTO toAlbumDTO(Album album) {
        return AlbumDTO.builder()
                .albumId(album.getAlbumId())
                .albumName(album.getAlbumName().getAlbumName())
                .albumImage(album.getAlbumImage())
                .createdDate(album.getCreatedDate())
                .build();
    }
    
}
