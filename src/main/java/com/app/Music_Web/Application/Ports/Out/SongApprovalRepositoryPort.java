package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.SongApproval;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

public interface SongApprovalRepositoryPort {
    SongApproval save(SongApproval songApproval);

    void updateStatusSong(Long songId, ApprovalStatus approvalStatus);
}
