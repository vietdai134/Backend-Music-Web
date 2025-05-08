package com.app.Music_Web.Infrastructure.Config;

import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GoogleCredentialsSetup {

    public static void setupCredentials() {
        try {
            // Lấy chuỗi base64 từ biến môi trường
            String base64Credentials = System.getenv("GOOGLE_CREDENTIALS_BASE64");

            if (base64Credentials == null || base64Credentials.isEmpty()) {
                System.out.println("No base64 credentials found in environment variable!");
                return;
            }

            // Giải mã chuỗi base64 thành byte[]
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);

            // Lưu nội dung vào file cred.json
            Files.write(Paths.get("/app/cred.json"), decodedBytes);

            System.out.println("Google credentials file saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

