package com.app.Music_Web.Domain.Exceptions.SongException;

import com.app.Music_Web.Domain.Exceptions.AppException;

public class SongNotFoundException extends AppException {
    public SongNotFoundException(Long songId) {
        super("Không tìm thấy bài hát với id: " + songId);
    }
    
}
