package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.RolePermissionRepositoryPort;
import com.app.Music_Web.Domain.Entities.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission,Long>, RolePermissionRepositoryPort {
    
}
