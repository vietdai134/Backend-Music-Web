package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.Follow;

public interface FollowRepositoryPort {
    Follow save(Follow follow);
}
