package com.app.Music_Web.API.Controllers;

import com.app.Music_Web.API.Request.SongRequest;
import com.app.Music_Web.API.Response.SongResponse;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.Ports.In.Song.FindAllSongService;
import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/songs")
public class SongController {
    private final FindAllSongService findAllSongService;
    private final SaveSongService saveSongService;
    public SongController(FindAllSongService findAllSongService, SaveSongService saveSongService) {
        this.saveSongService = saveSongService;
        this.findAllSongService = findAllSongService;
    }

    @PostMapping
    public SongResponse createSong(@RequestBody SongRequest request) {
        SongDTO savedSong = saveSongService.saveSong(request.getTitle(), 
                            request.getArtist(), request.getSong_image(),
                            request.getSource_url(),request.isDownloadable());
        // return new SongResponse(savedSong.getId(), savedSong.getTitle());
        return SongResponse.builder()
                .id(savedSong.getId())
                .title(savedSong.getTitle())
                .build();
    }

    @GetMapping("/all")
    public Page<SongResponse> getAllSongs(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.unsorted());
        Page<SongDTO> songs = findAllSongService.findAll(pageable);
        // return songs.map(song -> new SongResponse(song.getId(), song.getTitle()));
        return songs.map(song-> SongResponse.builder()
                                            .id(song.getId())
                                            .title(song.getTitle())
                                            .build());
    }

}