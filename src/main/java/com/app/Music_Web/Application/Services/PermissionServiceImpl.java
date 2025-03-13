package com.app.Music_Web.Application.Services;

import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.PermissionDTO;
import com.app.Music_Web.Application.Mapper.PermissionMapper;
import com.app.Music_Web.Application.Ports.In.Permission.SavePermissionService;
import com.app.Music_Web.Application.Ports.Out.PermissionRepositoryPort;
import com.app.Music_Web.Domain.Entities.Permission;

@Service
public class PermissionServiceImpl implements SavePermissionService {
    private final PermissionRepositoryPort permissionRepositoryPort;
    public PermissionServiceImpl(PermissionRepositoryPort permissionRepositoryPort) {
        this.permissionRepositoryPort = permissionRepositoryPort;
    }
    @Override
    public PermissionDTO savePermission(String permissionName, String description) {
        Permission permission = Permission.builder()
                .permissionName(permissionName)
                .description(description)
                .build();
        Permission savedPermission = permissionRepositoryPort.save(permission);
        return PermissionMapper.toDTO(savedPermission);
    }
}
