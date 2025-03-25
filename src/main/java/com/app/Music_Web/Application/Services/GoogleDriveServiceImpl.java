package com.app.Music_Web.Application.Services;

import java.io.IOException;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.app.Music_Web.Application.Ports.In.GoogleDrive.GoogleDriveService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;

import jakarta.annotation.PostConstruct;

@Service
public class GoogleDriveServiceImpl implements GoogleDriveService{
    private final GoogleCredentials googleCredentials;
    private final RestTemplate restTemplate;
    private static final String GOOGLE_DRIVE_FILES_URL = "https://www.googleapis.com/drive/v3/files";
    private static final String UPLOAD_URL = "https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart";

    public GoogleDriveServiceImpl(
                GoogleCredentials googleCredentials,
                RestTemplate restTemplate){
        this.googleCredentials=googleCredentials;
        this.restTemplate=restTemplate;
    }

    @PostConstruct
    public void init() {
        try {
            String accessToken=getAccessToken();
            System.out.println(accessToken);
            listDriveFiles(accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public String getAccessToken()throws IOException{
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
    
    public String listDriveFiles(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_DRIVE_FILES_URL,
                HttpMethod.GET,
                entity,
                String.class
        );

        String responseBody = response.getBody();
        System.out.println("📂 Danh sách file trên Google Drive:");
        System.out.println(response.getBody());
        return responseBody;
    }

    public String getFileInfo( String accessToken,String fileId) {
        String url = GOOGLE_DRIVE_FILES_URL + "/" + fileId;
        HttpHeaders headers = createHeaders(accessToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                new HttpEntity<>(headers), 
                String.class);

        String responseBody = response.getBody();
        System.out.println("📄 Thông tin bài hát: " + responseBody);
        return responseBody;
    }

    @Override
    public String uploadFile(byte[] fileData, String fileName, String accessToken) {
        HttpHeaders headers = createHeaders(accessToken);

        // Tạo MultiValueMap để chứa metadata và file
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // Thêm metadata (JSON)
        HttpHeaders metadataHeaders = new HttpHeaders();
        metadataHeaders.setContentType(MediaType.APPLICATION_JSON);
        String folderId = "1df1lvVIEwtsaJP2DOoxQlJiTgZViM3UR";
        String metadata = "{\"name\":\"" + fileName + "\",\"parents\":[\"" + folderId + "\"]}";
        HttpEntity<String> metadataPart = new HttpEntity<>(metadata, metadataHeaders);
        body.add("metadata", metadataPart);

        // Thêm file
        HttpHeaders fileHeaders = new HttpHeaders();
        fileHeaders.setContentType(MediaType.parseMediaType("audio/mpeg"));
        HttpEntity<byte[]> filePart = new HttpEntity<>(fileData, fileHeaders);
        body.add("file", filePart);

        // Tạo request entity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    UPLOAD_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            String responseBody = response.getBody();
            String fileId = extractFileIdFromResponse(responseBody);
            System.out.println("✅ Upload thành công: " + responseBody);
            return fileId;
        } catch (RestClientException e) {
            System.err.println("❌ Lỗi khi upload file: " + e.getMessage());
            throw e; // Ném lại để xử lý ở tầng trên nếu cần
        }
    }

    private String extractFileIdFromResponse(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);
            JsonNode idNode = root.path("id");
            return idNode.isMissingNode() ? null : idNode.asText();
        } catch (IOException e) {
            System.err.println("Lỗi khi parse response: " + responseBody);
            e.printStackTrace();
            return null;
        }
    }

    public String updateFileName(String accessToken, String fileId, String newName) {
        String url = GOOGLE_DRIVE_FILES_URL + "/" + fileId;
        HttpHeaders headers = createHeaders(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = "{\"name\":\"" + newName + "\"}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.PATCH, 
            entity, 
            String.class);

        // System.out.println("✏️ Cập nhật tên file thành công: " + response.getBody());
        String responseBody = response.getBody();
        System.out.println("✏️ Cập nhật tên file thành công: " + responseBody);
        return responseBody;
    }

    public void deleteFile(String accessToken,String fileId) {
        String url = GOOGLE_DRIVE_FILES_URL + "/" + fileId;
        HttpHeaders headers = createHeaders(accessToken);

        ResponseEntity<Void> response = restTemplate.exchange(
            url, 
            HttpMethod.DELETE, 
            new HttpEntity<>(headers), 
            Void.class);

        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            System.out.println("🗑️ Xóa file thành công");
        } else {
            System.out.println("❌ Lỗi khi xóa file");
        }
    }

    public byte[] downloadFile(String accessToken,String fileId) {
        String url = GOOGLE_DRIVE_FILES_URL + "/" + fileId + "?alt=media";
        HttpHeaders headers = createHeaders(accessToken);

        ResponseEntity<byte[]> response = restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            new HttpEntity<>(headers), 
            byte[].class);

        byte[] fileData = response.getBody();
        System.out.println("📥 Tải xuống thành công. Kích thước: " + fileData.length + " bytes");
        return fileData;
    }


}
