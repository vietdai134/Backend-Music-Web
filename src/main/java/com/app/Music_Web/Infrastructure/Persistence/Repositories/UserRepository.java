package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long>,UserRepositoryPort {

    
}
