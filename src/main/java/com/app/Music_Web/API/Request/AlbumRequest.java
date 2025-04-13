package com.app.Music_Web.API.Request;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRequest {
    private String albumName;
    private MultipartFile albumImage;

}
