package com.app.Music_Web.Application.Services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.app.Music_Web.Application.DTO.PermissionDTO;
import com.app.Music_Web.Application.Mapper.PermissionMapper;
import com.app.Music_Web.Application.Ports.In.Permission.DeletePermissionService;
import com.app.Music_Web.Application.Ports.In.Permission.FindPermissionService;
import com.app.Music_Web.Application.Ports.In.Permission.SavePermissionService;
import com.app.Music_Web.Application.Ports.In.Permission.UpdatePermissionService;
import com.app.Music_Web.Application.Ports.Out.PermissionRepositoryPort;
import com.app.Music_Web.Domain.Entities.Permission;

@Service
public class PermissionServiceImpl implements SavePermissionService,FindPermissionService,DeletePermissionService,UpdatePermissionService {
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

    @Override
    public Page<PermissionDTO> findAll(Pageable pageable) {
        Page<Permission> permissions = permissionRepositoryPort.findAll(pageable);
        return permissions.map(PermissionMapper::toDTO);
    }

    @Override
    public void deletePermission (Long permissionId) {
        Permission permission = permissionRepositoryPort.findByPermissionId(permissionId);
        if(permission==null){
            throw new RuntimeException("permission not found");
        }
        permissionRepositoryPort.delete(permission);            
    }

     @Override
    public PermissionDTO updatePermission (Long permissionId, String permissionName, String description) {
        // Tìm user theo userId
        Permission permission = permissionRepositoryPort.findByPermissionId(permissionId);
        if(permission==null){
            throw new RuntimeException("permission not found");
        }

        permission.setPermissionName(permissionName);
        permission.setDescription(description);

        // Lưu user đã cập nhật
        Permission updatedpermission = permissionRepositoryPort.save(permission);
        return PermissionMapper.toDTO(updatedpermission);
    }

    @Override
    public PermissionDTO findByPermissionId(Long permissionId) {
        Permission permission = permissionRepositoryPort.findByPermissionId(permissionId);
        if(permission==null){
            throw new RuntimeException("permission not found");
        }
        return PermissionMapper.toDTO(permission);
    }

    @Override
    public Page<PermissionDTO> searchByPermissionName(String keyword, Pageable pageable) {
        Page<Permission> permission = permissionRepositoryPort.searchByPermissionName(keyword, pageable);
        return permission.map(PermissionMapper::toDTO);
    }
    @Override
    public List<PermissionDTO> findAllPermissions() {
        List<Permission> permissions= permissionRepositoryPort.findAll();
        return permissions.stream().map(PermissionMapper::toDTO).toList();
    }
}
