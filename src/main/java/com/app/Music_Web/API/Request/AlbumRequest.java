package com.app.Music_Web.API.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRequest {
    private String albumName;
    private String albumImage;
    private Long userId;
}
