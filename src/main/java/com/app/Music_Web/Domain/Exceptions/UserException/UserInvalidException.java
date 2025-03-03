package com.app.Music_Web.Domain.Exceptions.UserException;

import com.app.Music_Web.Domain.Exceptions.AppException;

public class UserInvalidException extends AppException {
    public UserInvalidException(String message) {
        super(message);
    }
    
}
