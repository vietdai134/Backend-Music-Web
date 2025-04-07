package com.app.Music_Web.API.Response;
import java.util.Date;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistResponse {
    private Long playlistId;
    private String playlistName;
    private Date createdDate;
    private String createdBy;
}
