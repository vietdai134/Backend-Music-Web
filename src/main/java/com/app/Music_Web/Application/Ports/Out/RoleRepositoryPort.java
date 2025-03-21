package com.app.Music_Web.Application.Ports.Out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Domain.Entities.Role;


public interface RoleRepositoryPort {
    Page<Role> findAllWithPermissions(Pageable pageable);
    Page<Role> searchByRoleName(String keyword,Pageable pageable);

    Role save(Role role);
    void delete(Role role);

    List<Role> findAll();
    // List<Role> findRolesByUserId(Long userId);
    Role findByRoleName(String roleName);
    List<Role> findByRoleNameIn(List<String> roleNames);
    
    Optional<Role> findById(Long roleId);

}
