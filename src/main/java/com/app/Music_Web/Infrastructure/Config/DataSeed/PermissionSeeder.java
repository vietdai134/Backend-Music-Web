package com.app.Music_Web.Infrastructure.Config.DataSeed;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.Music_Web.Domain.Entities.Permission;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.PermissionRepository;

@Configuration
public class PermissionSeeder {
    @Bean
    CommandLineRunner seedPermissions (PermissionRepository permisionRepository){
        return args -> {
            if(permisionRepository.count()==0){
                System.out.println("Seeding initial data for roles...");

                List<Permission> Permissions = List.of(
                    Permission.builder()
                        .permissionName("Nghe")
                        .description("")
                        .build(),
                    Permission.builder()
                        .permissionName("Tải")
                        .description("")
                        .build(),
                    Permission.builder()
                        .permissionName("Upload")
                        .description("")
                        .build()      
                );

                permisionRepository.saveAll(Permissions);
                System.out.println("Successfully seeded 3 permissions into the database!");
            } else {
                System.out.println("permissions table already contains data. Skipping seeding.");
            }
            
        };
    }
}
