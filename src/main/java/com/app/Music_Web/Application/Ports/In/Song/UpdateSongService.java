package com.app.Music_Web.Application.Ports.In.Song;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.Music_Web.Application.DTO.SongDTO;

public interface UpdateSongService {
    SongDTO updateSong(Long songId, String title, String artist,String fileSongId,
                        MultipartFile songImg, List<String> genreNames
                        // boolean downloadable
                        ) throws Exception;
}
