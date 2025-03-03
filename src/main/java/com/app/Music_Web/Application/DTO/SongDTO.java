package com.app.Music_Web.Application.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongDTO {
    private Long id;
    private String title;
    private String artist;
    private String song_image;
    private String source_url;
    private boolean downloadable;
}