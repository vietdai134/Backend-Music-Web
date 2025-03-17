package com.app.Music_Web.Infrastructure.Config.DataSeed;

import com.app.Music_Web.Domain.Entities.Role;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.RoleRepository;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class RoleSeeder {
    @Bean
    @Order(1)
    CommandLineRunner seedRoles (RoleRepository roleRepository){
        return args -> {
            if(roleRepository.count()==0){
                System.out.println("Seeding initial data for roles...");

                List<Role> roles = List.of(
                    Role.builder()
                        .roleName("USER")
                        .description("Người dùng đã đăng nhập")
                        .build(),
                    Role.builder()
                        .roleName("PREMIUM_USER")
                        .description("Người dùng đã trả phí")
                        .build(),
                    Role.builder()
                        .roleName("MODERATOR")
                        .description("Người kiểm duyệt bài hát")
                        .build(),
                    Role.builder()
                        .roleName("ADMIN")
                        .description("Quản trị viên")
                        .build()      
                );

                roleRepository.saveAll(roles);
                System.out.println("Successfully seeded 3 Roles into the database!");
            } else {
                System.out.println("Roles table already contains data. Skipping seeding.");
            }
            
        };
    }
}
