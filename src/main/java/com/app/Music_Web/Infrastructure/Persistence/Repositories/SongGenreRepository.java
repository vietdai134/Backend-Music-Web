package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.SongGenreRepositoryPort;
import com.app.Music_Web.Domain.Entities.SongGenre;

public interface SongGenreRepository extends JpaRepository<SongGenre,Long>,SongGenreRepositoryPort{
    
}
