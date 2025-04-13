package com.app.Music_Web.API.Response;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumSongResponse {
    private Long albumSongId;
    private Long songId;
    private Long albumId;
}
