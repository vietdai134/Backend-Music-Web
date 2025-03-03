package com.app.Music_Web.Domain.Exceptions.GenreException;

import com.app.Music_Web.Domain.Exceptions.AppException;

public class GenreInvalidException extends AppException{
    public GenreInvalidException(String message) {
        super(message);
    }
    
}
