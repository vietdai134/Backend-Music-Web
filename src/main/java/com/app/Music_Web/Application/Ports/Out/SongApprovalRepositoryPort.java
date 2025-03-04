package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.SongApproval;

public interface SongApprovalRepositoryPort {
    SongApproval save(SongApproval songApproval);
}
