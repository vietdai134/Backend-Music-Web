package com.app.Music_Web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.app.Music_Web.Infrastructure.Persistence.Repositories")
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class MusicWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicWebApplication.class, args);
	}

}
