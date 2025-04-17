package com.app.Music_Web.Application.DTO;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterTempData {
    private String userName;
    private String email;
    private String password;
}
