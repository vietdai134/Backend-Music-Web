package com.app.Music_Web.Application.Ports.In.Redis;

public interface RedisUpdateService {
    void updateSong(String id, String title, String artist, String genre, String uploadDate, boolean downloadable,
                    String songImage, String fileSongId, String userName);

    void syncUpdateSong(Long songId);
}
