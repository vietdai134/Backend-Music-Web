package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.LikedSongRepositoryPort;
import com.app.Music_Web.Domain.Entities.LikedSong;

public interface LikedSongRepository extends JpaRepository<LikedSong,Long>, LikedSongRepositoryPort{
    
}
