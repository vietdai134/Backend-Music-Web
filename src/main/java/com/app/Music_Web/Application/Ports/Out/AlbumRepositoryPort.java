package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import com.app.Music_Web.Domain.Entities.Album;

public interface AlbumRepositoryPort {
    Album save(Album album);
    List<Album> findByUser_UserId(Long userId);
    Album findByAlbumId(Long albumId);

    void deleteByAlbumId(Long albumId);
    
}
