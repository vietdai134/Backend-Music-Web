package com.app.Music_Web.Application.DTO;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongRedisDTO {
    private Long songId;
    private String title;
    private String artist;
    private String songImage;
    private String fileSongId;
    // private boolean downloadable;
    private Date uploadDate;
    private String userName; 
    private String genresName;
}
