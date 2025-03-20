package com.app.Music_Web.API.Request;

import java.util.List;

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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class  UserUpdateRequest {
        private String userName;
        private String email;
        private String accountType;
        private List<String> roleNames;
    }
}



