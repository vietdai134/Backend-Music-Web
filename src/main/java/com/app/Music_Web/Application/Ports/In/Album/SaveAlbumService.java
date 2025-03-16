package com.app.Music_Web.Application.Ports.In.Album;

import com.app.Music_Web.Application.DTO.AlbumDTO;

public interface SaveAlbumService {
    AlbumDTO saveAlbum(String albumName, String albumImage, Long userId);
}
