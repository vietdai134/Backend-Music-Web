package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SongRepositoryPort {
    Page<Song> findAll(Pageable pageable); 
    Song save(Song song);
}
