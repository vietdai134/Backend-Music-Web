package com.app.Music_Web.Infrastructure.Config.DataSeed;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.app.Music_Web.Domain.Entities.Genre;
import com.app.Music_Web.Domain.ValueObjects.Genre.GenreName;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.GenreRepository;

@Configuration
public class GenreSeeder {
    @Bean
    @Order(1)
    CommandLineRunner seedGenres (GenreRepository genreRepository){
        return args ->{
            if (genreRepository.count() == 0) {
                System.out.println("Seeding initial data for Genre...");

                List<String> genreNames = List.of(
                    "Pop", "Dance Pop", "Teen Pop", "Electropop", "Bubblegum Pop", "Rock", "Alternative Rock",
                    "Hard Rock", "Soft Rock", "Punk Rock", "Progressive Rock", "Indie Rock", "Grunge","Metal", "Heavy Metal", 
                    "Thrash Metal", "Glam Rock", "Hip-Hop", "Rap", "Trap", "Boom Bap","Old School Hip-Hop", "Lo-Fi Hip-Hop", 
                    "Gangsta Rap", "Electronic", "EDM", "House","Techno", "Trance", "Dubstep", "Drum and Bass",
                    "Future Bass", "R&B", "Soul", "Neo-Soul", "Funk", "Motown","Jazz", "Smooth Jazz", "Swing", "Bebop", "Blues",
                    "Chicago Blues", "Delta Blues", "Country", "Folk", "Bluegrass", "Americana", "Classical", "Baroque", "Opera",
                     "Orchestral", "Instrumental", "Soundtrack", "Latin","Salsa", "Reggaeton", "Bachata", "Flamenco",
                     "Afrobeat", "K-Pop", "J-Pop", "Reggae","Dancehall", "Ska", "Gospel", "Christian Rock",
                     "Islamic Nasheed", "Buddhist Chant", "Lo-Fi", "Chillhop","Vaporwave", "Ambient", "Post-Rock"
                );

                List<Genre> genres = genreNames.stream()
                    .map(name -> Genre.builder().genreName(new GenreName(name)).build())
                    .collect(Collectors.toList());

                genreRepository.saveAll(genres);
                System.out.println("Successfully seeded genre into the database!");
            } else {
                System.out.println("Genres table already contains data. Skipping seeding.");
            }
        };
    }
}
