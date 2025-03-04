package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.SongLyricRepositoryPort;
import com.app.Music_Web.Domain.Entities.SongLyric;

public interface SongLyricRepository extends JpaRepository<SongLyric,Long>, SongLyricRepositoryPort{
    
}
