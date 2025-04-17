package com.app.Music_Web.Infrastructure.Config.DataSeed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.SongUpload;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.SongRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.SongUploadRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserRepository;

@Configuration
public class SongUploadSeeder {
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    public SongUploadSeeder(
        UserRepository userRepository,
        SongRepository songRepository
        ){
        this.userRepository=userRepository;
        this.songRepository=songRepository;
    }
    @Bean
    @Order(2)
    CommandLineRunner seedSongUploads(SongUploadRepository songUploadRepository) {
        return args -> {
            // Kiểm tra nếu bảng rỗng thì chèn dữ liệu
            if (songUploadRepository.count() == 0) {
                System.out.println("Seeding initial data for upload Songs...");

                List<Long> songIds = new ArrayList<>();
                for (long i=1;i<=21;i++){
                    songIds.add(i);
                }
                User user = userRepository.findByEmail(new UserEmail("vietnguyentran134@gmail.com"));
                for (Long songId: songIds){
                    Song song = songRepository.findBySongId(songId);

                    SongUpload songUpload= SongUpload.builder()
                        .song(song)
                        .user(user)
                        .uploadDate(new Date())
                        .build();
                    
                    songUploadRepository.save(songUpload);
                }
                System.out.println("Successfully seeded 10 songs into the database!");
            } else {
                System.out.println("Songs table already contains data. Skipping seeding.");
            }
        };
    }
}
