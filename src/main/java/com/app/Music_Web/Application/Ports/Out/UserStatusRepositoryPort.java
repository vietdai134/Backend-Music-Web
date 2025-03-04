package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.UserStatus;

public interface UserStatusRepositoryPort {
    UserStatus save(UserStatus userStatus);
}
