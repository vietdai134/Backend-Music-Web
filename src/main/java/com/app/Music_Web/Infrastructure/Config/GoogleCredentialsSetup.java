package com.app.Music_Web.Infrastructure.Config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class GoogleCredentialsSetup {

    @PostConstruct
    public void setupCredentials() {
        try {
            String base64Credentials = System.getenv("GOOGLE_CREDENTIALS_BASE64");
            if (base64Credentials == null || base64Credentials.isEmpty()) {
                System.err.println("Error: GOOGLE_CREDENTIALS_BASE64 is missing or empty");
                throw new IllegalStateException("GOOGLE_CREDENTIALS_BASE64 is required");
            }
            // Remove any whitespace or newlines
            base64Credentials = base64Credentials.trim();
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            Files.write(Paths.get("/app/cred.json"), decodedBytes);
            System.out.println("Google credentials file saved at /app/cred.json");
        } catch (Exception e) {
            System.err.println("Failed to create /app/cred.json: " + e.getMessage());
            throw new RuntimeException("Failed to create cred.json", e);
        }
    }
}