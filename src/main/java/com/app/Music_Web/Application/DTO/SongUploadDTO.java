package com.app.Music_Web.Application.DTO;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongUploadDTO {
    private Long uploadId;
    private Date uploadDate;
    private Long uploadedBy;
    private Long songId;
}
