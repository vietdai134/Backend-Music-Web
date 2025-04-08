package com.app.Music_Web.API.Response;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistSongResponse {
    private Long playlistSongId;
    private Long playlistId;
    private Long songId;
}
