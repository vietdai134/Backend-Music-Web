package com.app.Music_Web.API.Response;

import java.util.Date;

import com.app.Music_Web.Application.DTO.SongDTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongUploadResponse {
    private Long uploadId;
    private Date uploadDate;
    private SongDTO songDto;
    private String userName;
}
