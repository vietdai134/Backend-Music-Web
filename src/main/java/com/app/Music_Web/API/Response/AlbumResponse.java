package com.app.Music_Web.API.Response;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumResponse {
    private Long albumId;
    private String albumName;
    private String albumImage;
}
