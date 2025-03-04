package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.AlbumSongRepositoryPort;
import com.app.Music_Web.Domain.Entities.AlbumSong;

public interface AlbumSongRepository extends JpaRepository<AlbumSong,Long>, AlbumSongRepositoryPort{
    
}
