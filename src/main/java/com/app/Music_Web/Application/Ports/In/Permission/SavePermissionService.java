package com.app.Music_Web.Application.Ports.In.Permission;

import com.app.Music_Web.Application.DTO.PermissionDTO;

public interface SavePermissionService {
    PermissionDTO savePermission(String permissionName, String description);
}
