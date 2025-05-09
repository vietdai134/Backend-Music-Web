package com.app.Music_Web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.app.Music_Web.Infrastructure.Config.GoogleCredentialsSetup;


@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class MusicWebApplication {

	public static void main(String[] args) {
		// Setup Google Credentials
		GoogleCredentialsSetup googleCredentialsSetup = new GoogleCredentialsSetup();
		googleCredentialsSetup.setupCredentials();
		// Start the Spring Boot application
		SpringApplication.run(MusicWebApplication.class, args);
	}

}
