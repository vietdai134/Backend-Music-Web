package com.app.Music_Web.Application.Ports.In.Cloudinary;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    String uploadAvatar(MultipartFile file, String folder) throws IOException;
    void deleteAvatar(String publicId) throws IOException;
    String extractPublicIdFromUrl(String url) ;
    void deleteFolder(String folderPath) throws IOException ;
}
