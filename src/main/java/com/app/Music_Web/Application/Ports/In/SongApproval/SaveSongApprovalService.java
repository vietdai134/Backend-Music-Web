package com.app.Music_Web.Application.Ports.In.SongApproval;


import com.app.Music_Web.Application.DTO.SongApprovalDTO;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

public interface SaveSongApprovalService {
    SongApprovalDTO saveSongApproval(Long songId, Long userId, ApprovalStatus status);
}
