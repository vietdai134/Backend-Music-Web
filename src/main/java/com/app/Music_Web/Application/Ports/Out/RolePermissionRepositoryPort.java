package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.RolePermission;

public interface RolePermissionRepositoryPort {
    RolePermission save(RolePermission rolePermission);
}
