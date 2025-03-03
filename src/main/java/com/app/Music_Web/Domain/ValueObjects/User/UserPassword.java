package com.app.Music_Web.Domain.ValueObjects.User;

import com.app.Music_Web.Domain.Exceptions.UserException.UserInvalidException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class UserPassword {
    @Column(name = "password",nullable = false,length = 255)
    private String password;

    public UserPassword(String password) {
        if(password == null || password.isEmpty()){
            throw new UserInvalidException("User password is required");
        }
        if (password.length()>255){
            throw new UserInvalidException("User password is too long");
        }
        this.password = password.trim();
    }
}
