package com.app.Music_Web.Application.Mapper;

import java.util.stream.Collectors;

import com.app.Music_Web.Application.DTO.PermissionDTO;
import com.app.Music_Web.Application.DTO.RoleDTO;
import com.app.Music_Web.Domain.Entities.Role;

public class RoleMapper {
    public static RoleDTO toDTO(Role role) {
        return RoleDTO.builder()
            .roleId(role.getRoleId())
            .roleName(role.getRoleName())
            .description(role.getDescription())

            .permissions(role.getRolePermissions().stream()
                        // .map(rolePermission -> PermissionMapper.toDTO(rolePermission.getPermission()))
                        .map(rolePermission -> PermissionDTO.builder()
                        .permissionId(rolePermission.getPermission().getPermissionId())
                        .permissionName(rolePermission.getPermission().getPermissionName())
                        .description(rolePermission.getPermission().getDescription())
                        .assignedDate(rolePermission.getAssignedDate())
                        .build())
                        .collect(Collectors.toList()))
                      .build();
    }


}
