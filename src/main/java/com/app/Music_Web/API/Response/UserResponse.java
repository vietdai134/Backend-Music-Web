package com.app.Music_Web.API.Response;

import java.util.Date;
import java.util.List;

import com.app.Music_Web.Application.DTO.RoleDTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long userId;
    private String userName;
    private String email;
    private String accountType;
    private Date createdDate;
    private List<String> permissions;
    private List<RoleDTO> roles;
}
