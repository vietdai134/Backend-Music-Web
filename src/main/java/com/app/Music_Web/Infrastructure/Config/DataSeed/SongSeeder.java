package com.app.Music_Web.Infrastructure.Config.DataSeed;

import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.ValueObjects.Song.SongArtist;
import com.app.Music_Web.Domain.ValueObjects.Song.SongTitle;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.SongRepository;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SongSeeder {

    @Bean
    CommandLineRunner seedSongs(SongRepository songRepository) {
        return args -> {
            // Kiểm tra nếu bảng rỗng thì chèn dữ liệu
            if (songRepository.count() == 0) {
                System.out.println("Seeding initial data for Songs...");

                List<Song> songs = List.of(
                    Song.builder()
                        .title(new SongTitle("Bohemian Rhapsody"))
                        .artist(new SongArtist("Queen"))
                        .songImage("bohemian_rhapsody.jpg")
                        .sourceUrl("https://example.com/bohemian_rhapsody.mp3")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Take Five"))
                        .artist(new SongArtist("Dave Brubeck"))
                        .songImage("take_five.jpg")
                        .sourceUrl("https://example.com/take_five.mp3")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Lose Yourself"))
                        .artist(new SongArtist("Eminem"))
                        .songImage("lose_yourself.jpg")
                        .sourceUrl("https://example.com/lose_yourself.mp3")
                        .downloadable(false)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Moonlight Sonata"))
                        .artist(new SongArtist("Ludwig van Beethoven"))
                        .songImage("moonlight_sonata.jpg")
                        .sourceUrl("https://example.com/moonlight_sonata.mp3")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Blinding Lights"))
                        .artist(new SongArtist("The Weeknd"))
                        .songImage("blinding_lights.jpg")
                        .sourceUrl("https://example.com/blinding_lights.mp3")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Sweet Child O' Mine"))
                        .artist(new SongArtist("Guns N' Roses"))
                        .songImage("sweet_child_o_mine.jpg")
                        .sourceUrl("https://example.com/sweet_child_o_mine.mp3")
                        .downloadable(false)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Fly Me to the Moon"))
                        .artist(new SongArtist("Frank Sinatra"))
                        .songImage("fly_me_to_the_moon.jpg")
                        .sourceUrl("https://example.com/fly_me_to_the_moon.mp3")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Rolling in the Deep"))
                        .artist(new SongArtist("Adele"))
                        .songImage("rolling_in_the_deep.jpg")
                        .sourceUrl("https://example.com/rolling_in_the_deep.mp3")
                        .downloadable(false)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Viva La Vida"))
                        .artist(new SongArtist("Coldplay"))
                        .songImage("viva_la_vida.jpg")
                        .sourceUrl("https://example.com/viva_la_vida.mp3")
                        .downloadable(true)
                        .build(),

                    Song.builder()
                        .title(new SongTitle("Shape of You"))
                        .artist(new SongArtist("Ed Sheeran"))
                        .songImage("shape_of_you.jpg")
                        .sourceUrl("https://example.com/shape_of_you.mp3")
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