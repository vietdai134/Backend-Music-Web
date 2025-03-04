package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.UserStatusRepositoryPort;
import com.app.Music_Web.Domain.Entities.UserStatus;

public interface UserStatusRepository extends JpaRepository<UserStatus,Long>,UserStatusRepositoryPort{
    
}
