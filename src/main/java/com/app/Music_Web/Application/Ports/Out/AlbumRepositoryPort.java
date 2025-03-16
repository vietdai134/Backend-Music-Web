package com.app.Music_Web.Application.Ports.Out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Domain.Entities.Album;

public interface AlbumRepositoryPort {
    Album save(Album album);
    Page<Album> findByUser_UserId(Pageable pageable, Long userId);
}
