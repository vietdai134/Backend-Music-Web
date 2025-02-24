package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.Song;
import java.util.List;

public interface SongRepositoryPort {
    Song save(Song song);          
    List<Song> findByGenre(String genre); // Trả về Song, sẽ được chuyển sang DTO trong Service
}