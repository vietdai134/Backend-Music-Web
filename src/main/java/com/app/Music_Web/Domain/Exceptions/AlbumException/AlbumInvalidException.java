package com.app.Music_Web.Domain.Exceptions.AlbumException;

import com.app.Music_Web.Domain.Exceptions.AppException;

public class AlbumInvalidException extends AppException {
    public AlbumInvalidException(String message) {
        super(message);
    }
    
}
