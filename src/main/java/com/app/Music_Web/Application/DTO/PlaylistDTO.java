package com.app.Music_Web.Application.DTO;

import java.util.Date;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {
    private Long playlistId;
    private String playlistName;
    private Date createdDate;
    private String createdBy;
    private List<SongDTO> songs;
}
