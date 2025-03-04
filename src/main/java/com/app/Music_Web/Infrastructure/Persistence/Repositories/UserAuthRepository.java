package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.UserAuthRepositoryPort;
import com.app.Music_Web.Domain.Entities.UserAuth;

public interface UserAuthRepository extends JpaRepository<UserAuth,Long>,UserAuthRepositoryPort{
    
}
