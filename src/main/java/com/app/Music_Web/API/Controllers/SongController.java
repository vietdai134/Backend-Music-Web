package com.app.Music_Web.API.Controllers;

import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongsByGenreService;
import com.app.Music_Web.API.Request.SongRequest;
import com.app.Music_Web.API.Response.SongResponse;
import com.app.Music_Web.Application.DTO.SongDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    private final SaveSongService saveSongService;
    private final FindSongsByGenreService findSongsByGenreService;

    public SongController(SaveSongService saveSongService, FindSongsByGenreService findSongsByGenreService) {
        this.saveSongService = saveSongService;
        this.findSongsByGenreService = findSongsByGenreService;
    }

    // @PostMapping
    // public SongResponse createSong(@RequestBody SongRequest request) {
    //     SongDTO savedSong = saveSongService.saveSong(request.getTitle(), request.getArtist(), request.getGenre());
    //     return new SongResponse(savedSong.getId(), savedSong.getTitle());
    // }

    // @GetMapping("/genre/{genre}")
    // public List<SongResponse> getSongsByGenre(@PathVariable String genre) {
    //     List<SongDTO> songs = findSongsByGenreService.findSongsByGenre(genre);
    //     return songs.stream()
    //                 .map(song -> new SongResponse(song.getId(), song.getTitle()))
    //                 .collect(Collectors.toList());
    // }
}