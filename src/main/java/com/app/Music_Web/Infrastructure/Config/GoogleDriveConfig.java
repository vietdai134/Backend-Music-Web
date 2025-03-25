package com.app.Music_Web.Infrastructure.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import com.google.api.services.drive.DriveScopes;
import com.google.auth.oauth2.GoogleCredentials;

@Configuration
public class GoogleDriveConfig {

    @Value("${google.drive.credentials.path}")
    private String credentialsFilePath;

    @Bean
    public GoogleCredentials googleDriveCredentials() throws IOException {
        return GoogleCredentials.fromStream(new FileInputStream(credentialsFilePath))
                .createScoped(List.of(DriveScopes.DRIVE));
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
