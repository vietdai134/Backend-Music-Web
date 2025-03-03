package com.app.Music_Web.API.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongResponse {
    private Long id;
    private String title;


}