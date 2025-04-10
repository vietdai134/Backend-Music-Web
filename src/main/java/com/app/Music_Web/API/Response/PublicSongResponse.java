package com.app.Music_Web.API.Response;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublicSongResponse {
    private Long songId;
    private String title;
    private String artist;
    private String fileSongId;
    private String songImage;
    // private boolean downloadable;
    private Date uploadDate;
    private String userName;
    private String genresName;
}
