package com.app.Music_Web.Application.DTO;


import java.util.Date;
import java.util.List;

import com.app.Music_Web.Domain.Enums.AccountType;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long userId;
    private String userName;
    private String email;
    // private String password;
    private AccountType accountType;
    private Date createdDate;
    private String userAvatar;
    private List<RoleDTO> roles;
}
