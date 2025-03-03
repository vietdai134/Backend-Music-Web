package com.app.Music_Web.Domain.ValueObjects.User;

import com.app.Music_Web.Domain.Exceptions.UserException.UserInvalidException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class UserName {
    @Column(name = "user_name",nullable = false,length = 255)
    private String userName;

    public UserName(String userName) {
        if(userName == null || userName.isEmpty()){
            throw new UserInvalidException("User userName is required");
        }
        if (userName.length()>255){
            throw new UserInvalidException("User userName is too long");
        }
        this.userName = userName.trim();
    }
}
