package com.app.Music_Web.API.Request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String userName;
    private String email;
    private String password;
    private String accountType;
    private List<String> roleNames;
    private MultipartFile avatar;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class  UserUpdateRequest {
        private String userName;
        private String email;
        private String accountType;
        private List<String> roleNames;
        private MultipartFile avatar;
    }
}



