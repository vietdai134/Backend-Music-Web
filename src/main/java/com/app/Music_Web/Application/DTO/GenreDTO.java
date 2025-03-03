package com.app.Music_Web.Application.DTO;

import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenreDTO {
    private Long genreId;
    private String genreName;
    private List<SongDTO> songs;
}
