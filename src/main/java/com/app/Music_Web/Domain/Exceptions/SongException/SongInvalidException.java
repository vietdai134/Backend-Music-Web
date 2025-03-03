package com.app.Music_Web.Domain.Exceptions.SongException;

import com.app.Music_Web.Domain.Exceptions.AppException;

public class SongInvalidException extends AppException {
    public SongInvalidException(String message) {
        super(message);
    }
    
}
