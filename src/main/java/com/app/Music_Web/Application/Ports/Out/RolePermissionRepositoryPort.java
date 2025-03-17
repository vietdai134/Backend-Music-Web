package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import com.app.Music_Web.Domain.Entities.RolePermission;

public interface RolePermissionRepositoryPort {
    RolePermission save(RolePermission rolePermission);
    List<RolePermission> findByRole_RoleId(Long roleId);
}
