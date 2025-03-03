package com.app.Music_Web.Application.DTO;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FollowDTO {
    private Long followId;
    private Date followDate;
    private String follower;
    private String followed;
}
