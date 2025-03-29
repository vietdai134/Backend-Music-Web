package com.app.Music_Web.API.Response;

import java.util.Date;
import java.util.List;

import com.app.Music_Web.Application.DTO.GenreDTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongResponse {
    private Long songId;
    private String title;
    private String artist;
    private String fileSongId;
    private String songImage;
    private boolean downloadable;
    private Date approvedDate;
    private String userName;

    private List<GenreDTO> genres;

}