package com.app.Music_Web.Infrastructure.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Configuration
public class GoogleCredentialsSetup {

    @Bean
    public File googleCredentialsFile() {
        System.out.println("Starting GoogleCredentialsSetup...");
        try {
            String base64Credentials = System.getenv("GOOGLE_CREDENTIALS_BASE64");
            System.out.println("GOOGLE_CREDENTIALS_BASE64 present: " + (base64Credentials != null));
            if (base64Credentials == null || base64Credentials.isEmpty()) {
                System.err.println("Error: GOOGLE_CREDENTIALS_BASE64 is missing or empty");
                throw new IllegalStateException("GOOGLE_CREDENTIALS_BASE64 is required");
            }
            base64Credentials = base64Credentials.trim();
            System.out.println("Base64 length: " + base64Credentials.length());
            System.out.println("Decoding base64 credentials...");
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            System.out.println("Writing to /app/cred.json...");
            File credFile = new File("/app/cred.json");
            Files.write(Paths.get("/app/cred.json"), decodedBytes);
            System.out.println("Google credentials file saved at /app/cred.json");
            return credFile;
        } catch (Exception e) {
            System.err.println("Failed to create /app/cred.json: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create cred.json", e);
        }
    }
}