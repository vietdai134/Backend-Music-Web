package com.app.Music_Web.API.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SongRequest {
    private String title;
    private String artist;
    private String song_image;
    // private String songFileId;
    private byte[] songFileData;
    private boolean downloadable;

}