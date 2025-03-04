package com.app.Music_Web.API.Controllers;

import com.app.Music_Web.API.Request.SongRequest;
import com.app.Music_Web.API.Response.GenreResponse;
import com.app.Music_Web.API.Response.SongResponse;
import com.app.Music_Web.Application.DTO.GenreDTO;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.Ports.In.Song.DeleteSongService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongService;
import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/songs")
public class SongController {
    private final FindSongService findSongService;
    private final SaveSongService saveSongService;
    private final DeleteSongService deleteSongService;
    public SongController(FindSongService findSongService, 
                            SaveSongService saveSongService,
                            DeleteSongService deleteSongService) {
        this.saveSongService = saveSongService;
        this.findSongService = findSongService;
        this.deleteSongService=deleteSongService;
    }

    @PostMapping
    public SongResponse createSong(@RequestBody SongRequest request) {
        SongDTO savedSong = saveSongService.saveSong(request.getTitle(), 
                            request.getArtist(), request.getSong_image(),
                            request.getSource_url(),request.isDownloadable());
        return SongResponse.builder()
                .id(savedSong.getId())
                .title(savedSong.getTitle())
                .build();
    }

    @GetMapping("/all")
    public Page<SongResponse> getAllSongs(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.unsorted());
        Page<SongDTO> songs = findSongService.findAll(pageable);
        return songs.map(song-> SongResponse.builder()
                                            .id(song.getId())
                                            .title(song.getTitle())
                                            .build());
    }


    @GetMapping("/search")
    public ResponseEntity<?> getSongByTitle(@RequestParam String songTitle) {
        SongDTO song = findSongService.findBySongTitle(songTitle);
        if (song == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Song not found");
        }

        SongResponse response = SongResponse.builder()
                .id(song.getId())
                .title(song.getTitle())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long songId){
        deleteSongService.deleteSong(songId);
        return ResponseEntity.noContent().build();
    }
}