package com.app.Music_Web.Application.Ports.In.SongUpload;

import com.app.Music_Web.Application.DTO.SongUploadDTO;

public interface SaveSongUploadService {
    SongUploadDTO saveSongUpload(Long songId, Long userId);
}
