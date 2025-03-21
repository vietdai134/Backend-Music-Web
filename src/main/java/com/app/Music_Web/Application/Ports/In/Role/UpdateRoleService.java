package com.app.Music_Web.Application.Ports.In.Role;

import java.util.List;
import com.app.Music_Web.Application.DTO.RoleDTO;

public interface UpdateRoleService {
    RoleDTO updateRole (Long roleId, String roleName, String description,
                                List<String> permissionNames);
}
