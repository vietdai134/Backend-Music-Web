package com.app.Music_Web.Application.Ports.In.Album;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.Music_Web.Application.DTO.AlbumDTO;

public interface SaveAlbumService {
    AlbumDTO saveAlbum(String albumName, MultipartFile albumImage, Long userId);
    void saveSongToAlbum(Long songId, Long albumId);
    void saveMultipleSongsToAlbum(List<Long> songIds, Long albumId);
}
