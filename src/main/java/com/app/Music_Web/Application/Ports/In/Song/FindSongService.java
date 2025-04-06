package com.app.Music_Web.Application.Ports.In.Song;

import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.DTO.SongRedisDTO;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FindSongService {
    Page<SongDTO> findAll(Pageable pageable);
    Page<SongDTO> findAllWithStatus(ApprovalStatus status, Pageable pageable);
    Page<SongDTO> searchByTitleOrArtist(String keyword, ApprovalStatus status, 
                                        Pageable pageable);
    
    SongDTO findBySongTitle (String songTitle);
    SongDTO findBySongArtist (String songArtist);

    Long findByFileSongId(String fileSongId);

    SongDTO findBySongId(Long songId);

    SongRedisDTO findSongWithApproved(Long songId);

    Long findByUploadId(Long uploadId);
}