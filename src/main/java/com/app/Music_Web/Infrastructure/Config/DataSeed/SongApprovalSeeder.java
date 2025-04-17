package com.app.Music_Web.Infrastructure.Config.DataSeed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Entities.SongApproval;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.SongApprovalRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.SongRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserRepository;

@Configuration
public class SongApprovalSeeder {
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    public SongApprovalSeeder(
        UserRepository userRepository,
        SongRepository songRepository
        ){
        this.userRepository=userRepository;
        this.songRepository=songRepository;
    }

    @Bean
    @Order(2)
    CommandLineRunner seedSongApproves(SongApprovalRepository songApprovalRepository) {
        return args -> {
            // Kiểm tra nếu bảng rỗng thì chèn dữ liệu
            if (songApprovalRepository.count() == 0) {
                System.out.println("Seeding initial data for upload Songs...");

                List<Long> songIds = new ArrayList<>();
                for (long i=1;i<=21;i++){
                    songIds.add(i);
                }
                User user = userRepository.findByEmail(new UserEmail("vietnguyentran134@gmail.com"));
                for (Long songId: songIds){
                    Song song = songRepository.findBySongId(songId);

                    SongApproval songAprroval= SongApproval.builder()
                        .song(song)
                        .user(user)
                        .approvedDate(new Date())
                        .approvalStatus( ApprovalStatus.APPROVED)
                        .build();
                    
                        songApprovalRepository.save(songAprroval);
                }
                System.out.println("Successfully seeded 10 songs into the database!");
            } else {
                System.out.println("Songs table already contains data. Skipping seeding.");
            }
        };
    }
}
