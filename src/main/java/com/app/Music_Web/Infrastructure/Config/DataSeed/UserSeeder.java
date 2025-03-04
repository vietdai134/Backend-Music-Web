package com.app.Music_Web.Infrastructure.Config.DataSeed;

import java.util.Date;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Enums.AccountType;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Domain.ValueObjects.User.UserName;
import com.app.Music_Web.Domain.ValueObjects.User.UserPassword;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserRepository;

@Configuration
public class UserSeeder {
    @Bean
    CommandLineRunner seedUsers (UserRepository userRepository){
        return args -> {
            if(userRepository.count()==0){
                System.out.println("Seeding initial data for Songs...");

                List<User> users = List.of(
                    User.builder()
                        .userName(new UserName("viet"))
                        .email(new UserEmail("vietnguyentran134@gmail.com"))
                        .password(new UserPassword("123"))
                        .accountType(AccountType.ADMIN)
                        .createdDate(new Date())
                        .build(),

                    User.builder()
                        .userName(new UserName("vietdai"))
                        .email(new UserEmail("vntk134@gmail.com"))
                        .password(new UserPassword("123"))
                        .accountType(AccountType.NORMAL)
                        .createdDate(new Date())
                        .build(),
                    
                    User.builder()
                        .userName(new UserName("vietdai134"))
                        .email(new UserEmail("nguyentrandaiviet14112003@gmail.com"))
                        .password(new UserPassword("123"))
                        .accountType(AccountType.PREMIUM)
                        .createdDate(new Date())
                        .build()
                    
                );

                userRepository.saveAll(users);
                System.out.println("Successfully seeded 3 users into the database!");
            } else {
                System.out.println("Users table already contains data. Skipping seeding.");
            }
            
        };
    }
}
