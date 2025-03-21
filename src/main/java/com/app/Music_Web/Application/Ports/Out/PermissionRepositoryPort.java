package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Domain.Entities.Permission;

public interface PermissionRepositoryPort {
    Page<Permission> findAll(Pageable pageable);
    Page<Permission> searchByPermissionName(String keyword,Pageable pageable);

    Permission save(Permission permission);
    void delete(Permission permission);
    
    Permission findByPermissionId(Long permissionId);

    List<Permission> findByPermissionNameIn(List<String> permissionNames);

    // List<Permission> findAllPermission();
    List<Permission> findAll();
}
