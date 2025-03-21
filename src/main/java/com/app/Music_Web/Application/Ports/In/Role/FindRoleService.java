package com.app.Music_Web.Application.Ports.In.Role;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.app.Music_Web.Application.DTO.RoleDTO;

public interface FindRoleService {
    Page<RoleDTO> findAllWithPermissions(Pageable pageable);
    Page<RoleDTO> searchByRoleName(String keyword,Pageable pageable);

    List<RoleDTO> findAll();
    // List<RoleDTO> findRolesByUserId(Long userId);
    RoleDTO findByRoleName(String roleName);
    List<RoleDTO> findByRoleNameIn(List<String> roleName);
    RoleDTO findRoleById(Long roleId);
} 
