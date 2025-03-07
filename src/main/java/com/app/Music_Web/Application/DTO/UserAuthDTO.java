package com.app.Music_Web.Application.DTO;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAuthDTO {
    private Long authId;
    private String googleId;
    private String facebookId;
    private String accessToken;
    private String refreshToken;
    private Date refreshTokenExpiry;
    private String userName;

}
