package com.app.Music_Web.Application.Ports.Out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.app.Music_Web.Domain.Entities.SongUpload;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

public interface SongUploadRepositoryPort {
    SongUpload save(SongUpload songUpload);

    Page<Object[]> findAllWithSong(ApprovalStatus status, Pageable pageable);
    Page<Object[]> searchUploadByTitleOrArtist(String keyword,ApprovalStatus status,
                                             Pageable pageable);

    SongUpload findByUploadId(Long uploadId);
    Page<Object[]> findAllSongUploadWithApproveStatus(
                String approvalStatus,
                Long userId,
                Pageable pageable);
}
