package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.PermissionRepositoryPort;
import com.app.Music_Web.Domain.Entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission,Long>,PermissionRepositoryPort{
    
}
