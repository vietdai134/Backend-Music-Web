package com.app.Music_Web.Application.Ports.In.Song;

import com.app.Music_Web.Application.DTO.SongDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindSongService {
    Page<SongDTO> findAll(Pageable pageable);
    SongDTO findBySongTitle (String songTitle);
    SongDTO findBySongArtist (String songArtist);
}