package com.app.Music_Web.Application.Ports.In.SongUpload;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Application.DTO.SongRedisDTO;
import com.app.Music_Web.Application.DTO.SongUploadDTO;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

public interface FindSongUploadService {
    Page<SongUploadDTO> findAllWithSong (ApprovalStatus approvalStatus,
                                            Pageable pageable);

    Page<SongUploadDTO> searchUploadByTitleOrArtist(String keyword, ApprovalStatus status, 
                                    Pageable pageable);

    Page<SongRedisDTO> findAllSongUploadWithApproveStatus(
        ApprovalStatus approvalStatus,String email,
        Pageable pageable);
}
