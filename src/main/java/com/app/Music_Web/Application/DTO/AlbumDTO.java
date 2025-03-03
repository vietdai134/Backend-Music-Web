package com.app.Music_Web.Application.DTO;

import java.util.Date;
import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumDTO {
    private Long AlbumId;
    private String AlbumName;
    private String AlbumImage;
    private Date CreatedDate;
    private String CreatedBy;
    private List<SongDTO> Songs;
}
