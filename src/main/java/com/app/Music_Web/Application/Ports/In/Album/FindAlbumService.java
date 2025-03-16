package com.app.Music_Web.Application.Ports.In.Album;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Application.DTO.AlbumDTO;

public interface FindAlbumService {
    Page<AlbumDTO> findByUser_UserId(Pageable pageable, Long userId);
}
