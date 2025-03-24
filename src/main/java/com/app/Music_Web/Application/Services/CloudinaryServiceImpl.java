package com.app.Music_Web.Application.Services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.Music_Web.Application.Ports.In.Cloudinary.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class CloudinaryServiceImpl implements CloudinaryService{
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(
        Cloudinary cloudinary
    ){
        this.cloudinary=cloudinary;
    }

    @Override
    public String uploadAvatar(MultipartFile file, String folder) throws IOException {
        // Nén ảnh trước khi upload
        byte[] compressedImage = compressImage(file);

        @SuppressWarnings("unchecked")
        Map<String, Object> uploadParams = ObjectUtils.asMap(
            "folder", folder,
            "public_id", "avatar",
            "overwrite", true,
            "resource_type", "image"
        );

        @SuppressWarnings("unchecked")
        Map<String, Object> result = cloudinary.uploader().upload(compressedImage, uploadParams);

        // return (String) result.get("url"); // Trả về URL của ảnh
        String url = (String) result.get("url");
        // Ép buộc HTTPS
        if (url != null && url.startsWith("http://")) {
            url = url.replace("http://", "https://");
        }
        return url;
    }

    // Phương thức nén ảnh
    private byte[] compressImage(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Sử dụng Thumbnailator để resize và nén ảnh (cần thêm dependency)
        Thumbnails.of(image)
            .size(800, 800) // Resize về kích thước tối đa 800x800 (hoặc nhỏ hơn tùy nhu cầu)
            .outputQuality(0.8) // Chất lượng 80%
            .outputFormat("jpg") // Định dạng đầu ra
            .toOutputStream(baos);

        return baos.toByteArray();
    }

    @Override
    public void deleteAvatar(String publicId) throws IOException{
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    // Thêm phương thức xóa toàn bộ thư mục
    public void deleteFolder(String folderPath) throws IOException {
        try {
            // Xóa toàn bộ tài nguyên trong thư mục bằng prefix
            cloudinary.api().deleteResourcesByPrefix(folderPath, ObjectUtils.emptyMap());
            // Sau đó xóa chính thư mục
            cloudinary.api().deleteFolder(folderPath, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new IOException("Error deleting folder: " + folderPath, e);
        }
    }

    @Override
    public String extractPublicIdFromUrl(String url) {
        try {
            String[] parts = url.split("/upload/");
            if (parts.length > 1) {
                String path = parts[1];
                // Xử lý trường hợp có version (v123456) trong URL
                if (path.startsWith("v")) {
                    path = path.substring(path.indexOf("/") + 1);
                }
                return path.substring(0, path.lastIndexOf(".")); // Loại bỏ phần mở rộng file
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error extracting publicId from URL: " + url);
            return null;
        }
    }
}
