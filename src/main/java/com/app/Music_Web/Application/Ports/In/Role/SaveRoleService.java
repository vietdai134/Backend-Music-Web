package com.app.Music_Web.Application.Ports.In.Role;

import java.util.List;

import com.app.Music_Web.Application.DTO.RoleDTO;

public interface SaveRoleService {
    RoleDTO saveRole(String roleName, String roleDescription, List<String> permissionNames);
}
