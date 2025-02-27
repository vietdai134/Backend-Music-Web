package com.app.Music_Web.Infrastructure.Config.DataSeed;

// import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.SongRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SongSeeder {

    @Bean
    CommandLineRunner seedSongs(SongRepository songRepository) {
        return args -> {
            // Kiểm tra nếu bảng rỗng thì chèn dữ liệu
            // if (songRepository.count() == 0) {
            //     System.out.println("Seeding initial data for Songs...");

            //     songRepository.save(new Song("Shape of You", "Ed Sheeran", "Pop"));
            //     songRepository.save(new Song("Bohemian Rhapsody", "Queen", "Rock"));
            //     songRepository.save(new Song("Take Five", "Dave Brubeck", "Jazz"));
            //     songRepository.save(new Song("Lose Yourself", "Eminem", "Hip Hop"));
            //     songRepository.save(new Song("Moonlight Sonata", "Beethoven", "Classical"));
            //     songRepository.save(new Song("Blinding Lights", "The Weeknd", "Synth-pop"));
            //     songRepository.save(new Song("Sweet Child O' Mine", "Guns N' Roses", "Rock"));
            //     songRepository.save(new Song("Fly Me to the Moon", "Frank Sinatra", "Jazz"));
            //     songRepository.save(new Song("Rolling in the Deep", "Adele", "Soul"));
            //     songRepository.save(new Song("Viva La Vida", "Coldplay", "Alternative"));

            //     System.out.println("Successfully seeded 10 songs into the database!");
            // } else {
            //     System.out.println("Songs table already contains data. Skipping seeding.");
            // }
        };
    }
}