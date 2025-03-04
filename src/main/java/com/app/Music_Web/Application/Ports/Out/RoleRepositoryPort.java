package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.Role;

public interface RoleRepositoryPort {
    Role save(Role role);
}
