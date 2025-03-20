package com.app.Music_Web.Application.Ports.In.Role;

import java.util.List;

import com.app.Music_Web.Application.DTO.RoleDTO;

public interface FindRoleService {
    List<RoleDTO> findAll();
    // List<RoleDTO> findRolesByUserId(Long userId);
    RoleDTO findByRoleName(String roleName);
    List<RoleDTO> findByRoleNameIn(List<String> roleName);
} 
