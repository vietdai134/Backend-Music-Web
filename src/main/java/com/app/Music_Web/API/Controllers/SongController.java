package com.app.Music_Web.API.Controllers;

import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongsByGenreService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/songs")
public class SongController {
    // private final SaveSongService saveSongService;
    // private final FindSongsByGenreService findSongsByGenreService;

    // public SongController(SaveSongService saveSongService, FindSongsByGenreService findSongsByGenreService) {
    //     this.saveSongService = saveSongService;
    //     this.findSongsByGenreService = findSongsByGenreService;
    // }

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