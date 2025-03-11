package com.app.Music_Web.Application.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.RoleDTO;
import com.app.Music_Web.Application.Mapper.RoleMapper;
import com.app.Music_Web.Application.Ports.In.Role.FindRoleService;
import com.app.Music_Web.Application.Ports.In.Role.SaveRoleService;
import com.app.Music_Web.Application.Ports.Out.RoleRepositoryPort;
import com.app.Music_Web.Domain.Entities.Role;

@Service
public class RoleServiceImpl implements SaveRoleService, FindRoleService {
    private final RoleRepositoryPort roleRepositoryPort;
    public RoleServiceImpl(RoleRepositoryPort roleRepositoryPort) {
        this.roleRepositoryPort = roleRepositoryPort;
    }

    @Override
    public RoleDTO saveRole(String roleName, String roleDescription) {
        Role role = Role.builder()
                        .roleName(roleName)
                        .description(roleDescription)
                        .build();
        Role savedRole = roleRepositoryPort.save(role);
        return RoleMapper.toDTO(savedRole);
    }

    @Override
    public List<RoleDTO> findAll() {
        List<Role> roles = roleRepositoryPort.findAll();
        return roles.stream()
                    .map(RoleMapper::toDTO)
                    .toList();
    }
    
}
