package com.app.Music_Web.Application.Ports.In.SongApproval;

import com.app.Music_Web.Domain.Enums.ApprovalStatus;

public interface UpdateSongApprovalService {
    void changeStatusSong(Long songId, ApprovalStatus approvalStatus);
}
