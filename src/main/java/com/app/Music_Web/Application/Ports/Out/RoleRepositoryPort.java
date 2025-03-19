package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import com.app.Music_Web.Domain.Entities.Role;

public interface RoleRepositoryPort {
    Role save(Role role);
    List<Role> findAll();
    // List<Role> findRolesByUserId(Long userId);
    Role findByRoleName(String roleName);
}
