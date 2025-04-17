package com.app.Music_Web.Infrastructure.Persistence;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private final Long userId;
    private final String userName;
    private final String email;
    private final String authProvider;

    public CustomUserDetails(Long userId, String userName, String email, String password, 
                   String authProvider, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities); // email as username
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.authProvider=authProvider;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getAuthProvider() {
        return authProvider;
    }
}