package com.app.Music_Web.Application.Ports.In.Song;

import com.app.Music_Web.Application.DTO.SongDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindAllSongService {
    Page<SongDTO> findAll(Pageable pageable);
}