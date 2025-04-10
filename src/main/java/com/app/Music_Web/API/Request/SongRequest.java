package com.app.Music_Web.API.Request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SongRequest {
    private String title;
    private String artist;
    private MultipartFile songImage;
    private MultipartFile songFileData;
    // private boolean downloadable;

    private List<String> genreNames;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class  SongUpdateRequest {
        private String title;
        private String artist;
        private MultipartFile songImage;
        private String songFileId;
        // private boolean downloadable;
    
        private List<String> genreNames;
    }
}