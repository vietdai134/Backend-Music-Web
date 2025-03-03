package com.app.Music_Web.Application.Ports.In.Song;

import com.app.Music_Web.Application.DTO.SongDTO;

public interface SaveSongService {
    SongDTO saveSong(String title,String artist,String song_image, String source_url, boolean downloadable);
}