package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.Music_Web.Application.Ports.Out.PermissionRepositoryPort;
import com.app.Music_Web.Domain.Entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission,Long>,PermissionRepositoryPort{
    @Override
    @Query("SELECT p FROM Permission p " +
           "WHERE LOWER(p.permissionName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Permission> searchByPermissionName(@Param("keyword") String keyword,Pageable pageable);
}
