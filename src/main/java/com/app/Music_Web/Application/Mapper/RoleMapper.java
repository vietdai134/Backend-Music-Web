package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.RoleDTO;
import com.app.Music_Web.Domain.Entities.Role;

public class RoleMapper {
    public static RoleDTO toDTO(Role role) {
        return RoleDTO.builder()
                      .roleId(role.getRoleId())
                      .roleName(role.getRoleName())
                      .description(role.getDescription())
                      .build();
    }


}
