package com.app.Music_Web.Infrastructure.Config.DataSeed;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.app.Music_Web.Domain.Entities.Permission;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.PermissionRepository;

@Configuration
public class PermissionSeeder {
    @Bean
    @Order(3)
    CommandLineRunner seedPermissions (PermissionRepository permisionRepository){
        return args -> {
            if(permisionRepository.count()==0){
                System.out.println("Seeding initial data for roles...");

                List<Permission> Permissions = List.of(
                    Permission.builder()
                        .permissionName("CREATE_PLAYLIST")
                        .description("Tạo danh sách phát")
                        .build(),
                    Permission.builder()
                        .permissionName("HISTORY")
                        .description("Lịch sử nghe nhạc")
                        .build(),
                    Permission.builder()
                        .permissionName("DOWNLOAD_SONG")
                        .description("Tải nhạc")
                        .build(),
                    Permission.builder()
                        .permissionName("LIKE_SONG")
                        .description("Thích bài hát")
                        .build(),
                        Permission.builder()
                        .permissionName("UPLOAD_SONG")
                        .description("Đăng tải bài hát")
                        .build(),
                    Permission.builder()
                        .permissionName("EDIT_SONG")
                        .description("Chỉnh sửa thông tin bài hát đã đăng tải")
                        .build(),
                    Permission.builder()
                        .permissionName("CREATE_ALBUM")
                        .description("Tạo album")
                        .build(),
                    Permission.builder()
                        .permissionName("MODERATE_SONG")
                        .description("Kiểm duyệt bài hát ở trang admin")
                        .build(),
                    Permission.builder()
                        .permissionName("SYSTEM_MANAGEMENT")
                        .description("Quản lý hệ thống ở trang admin")
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
