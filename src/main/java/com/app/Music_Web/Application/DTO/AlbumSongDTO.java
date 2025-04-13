package com.app.Music_Web.Application.DTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumSongDTO {
    private Long albumSongId;
    private Long albumId;
    private Long songId;
}
