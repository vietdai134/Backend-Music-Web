package com.app.Music_Web.Infrastructure.Persistence.Repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.Music_Web.Application.Ports.Out.RoleRepositoryPort;
import com.app.Music_Web.Domain.Entities.Role;

public interface RoleRepository extends JpaRepository<Role,Long>, RoleRepositoryPort{
    @Override
    @Query("SELECT DISTINCT r FROM Role r " +
            "LEFT JOIN FETCH r.rolePermissions rp " +
            "LEFT JOIN FETCH rp.permission")
    Page<Role> findAllWithPermissions(Pageable pageable);

    @Override
    @Query("SELECT r FROM Role r " +
           "WHERE LOWER(r.roleName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Role> searchByRoleName(@Param("keyword") String keyword,Pageable pageable);
}
