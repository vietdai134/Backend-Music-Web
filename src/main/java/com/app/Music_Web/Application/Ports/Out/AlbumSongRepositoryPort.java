package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import com.app.Music_Web.Domain.Entities.AlbumSong;

public interface AlbumSongRepositoryPort {
    AlbumSong save(AlbumSong albumSong);
    boolean existsBySong_SongIdAndAlbum_AlbumId(Long songId, Long albumId);
    void deleteBySong_SongIdAndAlbum_AlbumId(Long songId, Long albumId);
    List<AlbumSong> findByAlbum_AlbumId(Long albumId);
}
