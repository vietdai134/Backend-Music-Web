package com.app.Music_Web.Infrastructure.Config.DataSeed;

import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.ValueObjects.Song.SongArtist;
import com.app.Music_Web.Domain.ValueObjects.Song.SongTitle;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.SongRepository;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class SongSeeder {

    @Bean
    @Order(1)
    CommandLineRunner seedSongs(SongRepository songRepository) {
        return args -> {
            // Kiểm tra nếu bảng rỗng thì chèn dữ liệu
            if (songRepository.count() == 0) {
                System.out.println("Seeding initial data for Songs...");

                List<Song> songs = List.of(
                    Song.builder()
                        .title(new SongTitle("Playing God"))
                        .artist(new SongArtist("Polyphia"))
                        .songImage("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743621896/songs/songImages/1UCICxA-4QVAz_OmZxVJwmVUR4L-ogev8/avatar.jpg")
                        .fileSongId("1UCICxA-4QVAz_OmZxVJwmVUR4L-ogev8")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("O.D"))
                        .artist(new SongArtist("Polyphia"))
                        .songImage("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743622134/songs/songImages/1JPdDiXH_rXMdQnE0nkv4EekkyLaMKSJX/avatar.jpg")
                        .fileSongId("1JPdDiXH_rXMdQnE0nkv4EekkyLaMKSJX")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Ego Death feat Steve Vai"))
                        .artist(new SongArtist("Polyphia"))
                        .songImage("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743622620/songs/songImages/16dXr0iN15P2h2Gis9zvafMuuplYCkY5w/avatar.jpg")
                        .fileSongId("16dXr0iN15P2h2Gis9zvafMuuplYCkY5w")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("ABC feat Sophia Black"))
                        .artist(new SongArtist("Polyphia"))
                        .songImage("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743622876/songs/songImages/1aTGJjopIryBkdTa-4pFPBtNyMQOatcXV/avatar.jpg")
                        .fileSongId("1aTGJjopIryBkdTa-4pFPBtNyMQOatcXV")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Crystallized"))
                        .artist(new SongArtist("Camellia"))
                        .songImage("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743623166/songs/songImages/1UxQI259ivOy-I4bI8byDc3E5syt_ecNY/avatar.jpg")
                        .fileSongId("1UxQI259ivOy-I4bI8byDc3E5syt_ecNY")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Finorza"))
                        .artist(new SongArtist("Camellia feat Nanahira"))
                        .songImage("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743623434/songs/songImages/1o3KgSQmpWPfMl0FBStJfxPomfhDC2U0d/avatar.jpg")
                        .fileSongId("1o3KgSQmpWPfMl0FBStJfxPomfhDC2U0d")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Louder than steel"))
                        .artist(new SongArtist("Ryu-5150"))
                        .songImage("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743623861/songs/songImages/1PjZsxEx-K6qVXZn6IYkqHI2u1wVqebyE/avatar.jpg")
                        .fileSongId("1PjZsxEx-K6qVXZn6IYkqHI2u1wVqebyE")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Death Piano"))
                        .artist(new SongArtist("Xi"))
                        .songImage("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743624220/songs/songImages/1ja7ZEp6lFlBqCVJZXefLfg_bhp6Yuv12/avatar.jpg")
                        .fileSongId("1ja7ZEp6lFlBqCVJZXefLfg_bhp6Yuv12")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Blue Zenith"))
                        .artist(new SongArtist("Xi"))
                        .songImage("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743624434/songs/songImages/16hV7O4FAV3a_SzSlSliWJobNmHU5KoVP/avatar.jpg")
                        .fileSongId("16hV7O4FAV3a_SzSlSliWJobNmHU5KoVP")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Yue"))
                        .artist(new SongArtist("Kozato"))
                        .songImage("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743624773/songs/songImages/1iD54YSKe4Rr23HaKpIIeLQboL38qNIOy/avatar.jpg")
                        .fileSongId("1iD54YSKe4Rr23HaKpIIeLQboL38qNIOy")
                        .downloadable(true)
                        .build()
                );

                songRepository.saveAll(songs);
                System.out.println("Successfully seeded 10 songs into the database!");
            } else {
                System.out.println("Songs table already contains data. Skipping seeding.");
            }
        };
    }
}