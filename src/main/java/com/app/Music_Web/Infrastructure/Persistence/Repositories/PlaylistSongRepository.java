package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Music_Web.Application.Ports.Out.PlaylistSongRepositoryPort;
import com.app.Music_Web.Domain.Entities.PlaylistSong;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong,Long>,PlaylistSongRepositoryPort{
    
}
