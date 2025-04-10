package com.app.Music_Web.Application.Ports.In.Redis;

public interface RedisSaveService {
    void addSong(String id, String title, String artist, 
        String genre, String uploadDate, 
        // boolean downloadable,
        String songImage, String fileSongId, String userName);

    void syncAddSong(Long songId);

}
