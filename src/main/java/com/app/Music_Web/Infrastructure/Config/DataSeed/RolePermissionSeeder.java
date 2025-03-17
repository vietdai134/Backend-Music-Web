package com.app.Music_Web.Infrastructure.Config.DataSeed;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.app.Music_Web.Domain.Entities.Permission;
import com.app.Music_Web.Domain.Entities.Role;
import com.app.Music_Web.Domain.Entities.RolePermission;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.PermissionRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.RolePermissionRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.RoleRepository;

@Configuration
public class RolePermissionSeeder {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    public RolePermissionSeeder(PermissionRepository permissionRepository, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }
    @Bean
    @Order(5)
    CommandLineRunner seedRolePermissions(RolePermissionRepository rolePermissionRepository) {
        return args -> {
            if (rolePermissionRepository.count() == 0) {
                System.out.println("Seeding initial data for role permissions...");
    
                // Map<RoleID, List<PermissionID>>
                Map<Long, List<Long>> rolePermissionMap = new HashMap<>();
                rolePermissionMap.put(1L, List.of(1L, 2L, 3L, 4L));
                rolePermissionMap.put(2L, List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L));
                rolePermissionMap.put(4L, List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L));
    
                for (Map.Entry<Long, List<Long>> entry : rolePermissionMap.entrySet()) {
                    Long roleId = entry.getKey();
                    List<Long> permissionIds = entry.getValue();
    
                    Role role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new IllegalArgumentException("Role không tồn tại với ID: " + roleId));
    
                    for (Long permissionId : permissionIds) {
                        Permission permission = permissionRepository.findById(permissionId)
                                .orElseThrow(() -> new IllegalArgumentException("Permission không tồn tại với ID: " + permissionId));
    
                        // Tạo mới RolePermission
                        RolePermission rolePermission = RolePermission.builder()
                                .permission(permission)
                                .role(role)
                                .assignedDate(new Date())
                                .build();
    
                        rolePermissionRepository.save(rolePermission);
                    }
                }
                System.out.println("Successfully seeded role permissions into the database!");
            } else {
                System.out.println("Role permissions table already contains data. Skipping seeding.");
            }
        };
    }
    
}
