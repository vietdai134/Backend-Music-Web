package com.app.Music_Web.Application.Ports.In.GoogleDrive;

import java.io.IOException;

public interface GoogleDriveService {
    String getAccessToken()throws IOException;
    String uploadFile(byte[] fileData, String fileName, String accessToken);
    String listDriveFiles(String accessToken);
    String getFileInfo( String accessToken,String fileId);
    String updateFileName(String accessToken, String fileId, String newName);
    void deleteFile(String accessToken,String fileId);
    byte[] downloadFile(String accessToken,String fileId);
    String getFileName(String accessToken, String fileId);
}
