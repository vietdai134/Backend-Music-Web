package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.PlaylistRepositoryPort;
import com.app.Music_Web.Domain.Entities.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist,Long>,PlaylistRepositoryPort{
    
}
