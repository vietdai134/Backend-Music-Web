package com.app.Music_Web.Infrastructure.Config.DataSeed;

import java.util.Date;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.app.Music_Web.Domain.Entities.Role;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Entities.UserRole;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.RoleRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserRoleRepository;

@Configuration
public class UserRoleSeeder {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public UserRoleSeeder(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Bean
    @Order(4)
    CommandLineRunner seedUserRoles (UserRoleRepository userRoleRepository){
        return args -> {
            if(userRoleRepository.count()==0){
                System.out.println("Seeding initial data for user roles...");
                Map<Long, Long> userRoleMap = Map.of(
                    1L, 4L,  
                    2L, 1L,  
                    3L, 2L   
                );
                for (Map.Entry<Long, Long> entry : userRoleMap.entrySet()) {
                    Long userId = entry.getKey();
                    Long roleId = entry.getValue();
        
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User không tồn tại với ID: " + userId));
        
                    Role role = roleRepository.findById(roleId)
                            .orElseThrow(() -> new IllegalArgumentException("Role không tồn tại với ID: " + roleId));
        
                    // Tạo mới UserRole
                    UserRole userRole = UserRole.builder()
                            .user(user)
                            .role(role)
                            .grantedDate(new Date())
                            .build();
        
                    userRoleRepository.save(userRole);
                }
                System.out.println("Successfully seeded 3 user roles into the database!");
            } else {
                System.out.println("User roles table already contains data. Skipping seeding.");

            }
        };
    }
}
