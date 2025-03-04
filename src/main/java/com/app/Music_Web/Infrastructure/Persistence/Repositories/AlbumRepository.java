package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.AlbumRepositoryPort;
import com.app.Music_Web.Domain.Entities.Album;

public interface AlbumRepository extends JpaRepository<Album,Long>,AlbumRepositoryPort{
    
}
