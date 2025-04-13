package com.app.Music_Web.Application.Ports.In.Album;

import java.util.List;

import com.app.Music_Web.Application.DTO.AlbumDTO;
import com.app.Music_Web.Application.DTO.AlbumSongDTO;

public interface FindAlbumService {
    List<AlbumDTO> findAlbumByUserId(Long userId);
    List<AlbumSongDTO> findSongIdsByAlbumId(Long albumId);
}
