package com.app.Music_Web.API.Request;
import lombok.*;

@Getter
@Setter
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
