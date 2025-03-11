package com.app.Music_Web.Infrastructure.Config.DataSeed;

import com.app.Music_Web.Domain.Entities.Role;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.RoleRepository;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleSeeder {
    @Bean
    CommandLineRunner seedRoles (RoleRepository roleRepository){
        return args -> {
            if(roleRepository.count()==0){
                System.out.println("Seeding initial data for roles...");

                List<Role> roles = List.of(
                    Role.builder()
                        .roleName("NORMAL")
                        .description("Có ")
                        .build(),
                    Role.builder()
                        .roleName("PREMIUM")
                        .description("")
                        .build(),
                    Role.builder()
                        .roleName("ADMIN")
                        .description("Có tất cả quyền")
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
