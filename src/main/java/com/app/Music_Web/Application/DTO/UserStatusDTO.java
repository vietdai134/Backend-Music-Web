package com.app.Music_Web.Application.DTO;

import java.util.Date;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusDTO {
    private Long statusId;
    private Date lastLogin;
    private boolean isActive;
    private String userName;
}
