package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Domain.Entities.Song;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long>, SongRepositoryPort {
    List<Song> findByGenre(String genre);
}