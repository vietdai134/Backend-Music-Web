package com.app.Music_Web.Infrastructure.Persistence.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.RoleRepositoryPort;
import com.app.Music_Web.Domain.Entities.Role;

public interface RoleRepository extends JpaRepository<Role,Long>, RoleRepositoryPort{

}
