package com.app.Music_Web.Application.Ports.In.Permission;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Application.DTO.PermissionDTO;

public interface FindPermissionService {
    Page<PermissionDTO> findAll(Pageable pageable);

    List<PermissionDTO> findAllPermissions();
    PermissionDTO findByPermissionId(Long permissionId);
    Page<PermissionDTO> searchByPermissionName(String keyword, Pageable pageable);
}
