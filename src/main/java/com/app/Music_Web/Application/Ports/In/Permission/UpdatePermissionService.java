package com.app.Music_Web.Application.Ports.In.Permission;

import com.app.Music_Web.Application.DTO.PermissionDTO;

public interface UpdatePermissionService {
    PermissionDTO updatePermission (Long permissionId, String permissionName, String description);
}
