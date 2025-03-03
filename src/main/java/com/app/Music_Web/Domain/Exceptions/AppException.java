package com.app.Music_Web.Domain.Exceptions;

public abstract class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }
    
}
