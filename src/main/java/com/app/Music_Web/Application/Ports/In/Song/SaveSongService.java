package com.app.Music_Web.Application.Ports.In.Song;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.Music_Web.Application.DTO.SongDTO;

public interface SaveSongService {
    // SongDTO saveSong(String title,String artist,String song_image, String source_url, boolean downloadable);
    SongDTO saveSong(String title, String artist, MultipartFile songImage, 
                    String fileSongId,List<String> genreNames ,
                    boolean downloadable) throws Exception;
}