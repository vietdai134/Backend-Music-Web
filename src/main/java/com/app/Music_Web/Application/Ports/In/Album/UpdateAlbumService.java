package com.app.Music_Web.Application.Ports.In.Album;

import org.springframework.web.multipart.MultipartFile;

public interface UpdateAlbumService {
    void updateAlbum(Long albumId, String albumName, MultipartFile albumImage, Long userId);
}
