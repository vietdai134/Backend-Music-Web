package com.app.Music_Web.Application.DTO;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongUploadDTO {
    private Long uploadId;
    private Date uploadDate;
    private String uploadedBy;
    private Long songId;
}
