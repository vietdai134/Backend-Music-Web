package com.app.Music_Web.Domain.ValueObjects.User;

import com.app.Music_Web.Domain.Exceptions.UserException.UserInvalidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
@Embeddable
public class UserEmail {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    public UserEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new UserInvalidException("Email không được để trống.");
        }
        if (email.length() > 255) {
            throw new UserInvalidException("Email không được dài quá 255 ký tự.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new UserInvalidException("Email không hợp lệ.");
        }
        this.email = email.trim().toLowerCase(); // Normalize email
    }
}
