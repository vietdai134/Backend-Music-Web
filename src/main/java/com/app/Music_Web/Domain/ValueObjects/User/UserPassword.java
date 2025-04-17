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
    @Column(name = "password",nullable = true,length = 255)
    private String password;

    public UserPassword(String password) {
        if (password != null) { // Chỉ kiểm tra nếu password không null
            if (password.isEmpty()) {
                throw new UserInvalidException("User password cannot be empty");
            }
            if (password.length() > 255) {
                throw new UserInvalidException("User password is too long");
            }
            this.password = password.trim();
        } else {
            this.password = null; // Cho phép null cho người dùng Google
        }
    }
}
