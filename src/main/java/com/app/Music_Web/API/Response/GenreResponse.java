package com.app.Music_Web.API.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenreResponse {
    private Long genreId;
    private String genreName;
}
