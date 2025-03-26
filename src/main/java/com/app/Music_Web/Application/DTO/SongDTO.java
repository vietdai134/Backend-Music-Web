package com.app.Music_Web.Application.DTO;

import java.util.Date;
import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongDTO {
    private Long songId;
    private String title;
    private String artist;
    private String songImage;
    private String fileSongId;
    private boolean downloadable;
    // private SongApprovalDTO songApprovalDTO;
    private Date approvedDate;
    private List<GenreDTO> genres;
    private String userName;
}