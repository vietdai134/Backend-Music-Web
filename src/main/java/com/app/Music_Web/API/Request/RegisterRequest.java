package com.app.Music_Web.API.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String userName;
    private String email;
    private String password;
}
