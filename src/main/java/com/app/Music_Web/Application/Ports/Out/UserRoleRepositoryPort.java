package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.UserRole;

public interface UserRoleRepositoryPort {
    UserRole save(UserRole userRole);
}
