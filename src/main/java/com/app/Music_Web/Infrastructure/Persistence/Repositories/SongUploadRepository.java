package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.SongUploadRepositoryPort;
import com.app.Music_Web.Domain.Entities.SongUpload;

public interface SongUploadRepository extends JpaRepository<SongUpload,Long>,SongUploadRepositoryPort{
    
}
