package com.app.Music_Web.Application.DTO;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikedSongDTO {
    private Long likeId;
    private Date likedDate;
    private Long songId;
    private Long userId;
}
