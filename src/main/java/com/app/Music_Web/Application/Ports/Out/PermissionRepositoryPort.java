package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.Permission;

public interface PermissionRepositoryPort {
    Permission save(Permission permission);
}
