package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.UserRoleRepositoryPort;
import com.app.Music_Web.Domain.Entities.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole,Long>,UserRoleRepositoryPort{
    
}
