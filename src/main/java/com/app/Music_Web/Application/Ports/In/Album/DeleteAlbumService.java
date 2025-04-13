package com.app.Music_Web.Application.Ports.In.Album;

public interface DeleteAlbumService {
    void deleteAlbum(Long albumId);
    void deleteSongFromAlbum(Long songId, Long albumId);
}
