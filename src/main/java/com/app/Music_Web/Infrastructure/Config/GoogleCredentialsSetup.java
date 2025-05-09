package com.app.Music_Web.Infrastructure.Config;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Component
public class GoogleCredentialsSetup {
    private static final String ENV_VARIABLE = "GOOGLE_CREDENTIALS_BASE64";
    private static final String OUTPUT_FILE_PATH = "cred.json";

    @PostConstruct
    public void init() throws IOException {
        String base64 = System.getenv(ENV_VARIABLE);

        if (base64 == null || base64.isEmpty()) {
            throw new IllegalStateException("Environment variable " + ENV_VARIABLE + " is not set.");
        }

        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        Files.write(Paths.get(OUTPUT_FILE_PATH), decodedBytes);
        System.out.println("cred.json has been written successfully.");
    }
}

