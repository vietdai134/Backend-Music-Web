package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.PermissionDTO;
import com.app.Music_Web.Domain.Entities.Permission;

public class PermissionMapper {
    public static PermissionDTO toDTO(Permission permission) {
        return PermissionDTO.builder()
                .permissionId(permission.getPermissionId())
                .permissionName(permission.getPermissionName())
                .description(permission.getDescription())
                .build();
    }
}
