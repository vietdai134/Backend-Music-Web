package com.app.Music_Web.Application.Ports.In.Public;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Application.DTO.SongRedisDTO;

public interface FindService {
    Page<SongRedisDTO> findAllSong(Pageable pageable);
}
