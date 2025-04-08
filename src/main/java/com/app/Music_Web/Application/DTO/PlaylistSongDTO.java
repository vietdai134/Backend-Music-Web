package com.app.Music_Web.Application.DTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistSongDTO {
    private Long playlistSongId;
    private Long playlistId;
    private Long songId;
}
