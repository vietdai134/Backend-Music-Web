package com.app.Music_Web.Application.DTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongLyricDTO {
    private Long lyricsId;
    private String lyric;
    private Long songId;
}
