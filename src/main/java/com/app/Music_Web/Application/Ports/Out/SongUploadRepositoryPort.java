package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.SongUpload;

public interface SongUploadRepositoryPort {
    SongUpload save(SongUpload songUpload);
}
