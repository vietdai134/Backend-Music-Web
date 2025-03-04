package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.FollowRepositoryPort;
import com.app.Music_Web.Domain.Entities.Follow;

public interface FollowRepository extends JpaRepository<Follow,Long>,FollowRepositoryPort {
    
}
