package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.PlaylistSongRepositoryPort;
import com.app.Music_Web.Domain.Entities.PlaylistSong;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong,Long>,PlaylistSongRepositoryPort{
    
}
