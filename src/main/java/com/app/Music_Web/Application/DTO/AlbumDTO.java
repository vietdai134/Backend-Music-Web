package com.app.Music_Web.Application.DTO;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumDTO {
    private Long albumId;
    private String albumName;
    private String albumImage;
    private Date createdDate;
}
