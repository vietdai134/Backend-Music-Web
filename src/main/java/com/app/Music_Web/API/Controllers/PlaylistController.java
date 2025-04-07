package com.app.Music_Web.API.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.PlaylistRequest;
import com.app.Music_Web.API.Response.PlaylistResponse;
import com.app.Music_Web.Application.DTO.PlaylistDTO;
import com.app.Music_Web.Application.Ports.In.Playlist.FindPlaylistService;
import com.app.Music_Web.Application.Ports.In.Playlist.SavePlaylistService;
import com.app.Music_Web.Infrastructure.Persistence.CustomUserDetails;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {
    private final SavePlaylistService savePlaylistService;
    private final FindPlaylistService findPlaylistService;
    public PlaylistController(
        SavePlaylistService savePlaylistService,
        FindPlaylistService findPlaylistService) {
        this.savePlaylistService = savePlaylistService;
        this.findPlaylistService=findPlaylistService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<PlaylistResponse> createPlaylist(
        @RequestBody PlaylistRequest request,
        @AuthenticationPrincipal UserDetails userDetails) {
        
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        String email= customUserDetails.getEmail();

        PlaylistDTO savedPlaylist = savePlaylistService.createPlaylist(request.getPlayListName(),email);

        PlaylistResponse response = PlaylistResponse.builder()
                .playlistId(savedPlaylist.getPlaylistId())
                .playlistName(savedPlaylist.getPlaylistName())
                .createdDate(savedPlaylist.getCreatedDate())
                .createdBy(savedPlaylist.getCreatedBy())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/add-song")
    public ResponseEntity<Void> addSongToPlaylist(
        @RequestParam Long songId,
        @RequestParam Long playlistId
    ){
        savePlaylistService.saveSongToPlaylist(songId, playlistId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-multi-song")
    public ResponseEntity<Void> addMultiSongToPlaylist(
        @RequestParam List<Long> songIds,
        @RequestParam Long playlistId
    ){
        savePlaylistService.saveMultipleSongsToPlaylist(songIds, playlistId); 
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> createPlaylist(
        @AuthenticationPrincipal UserDetails userDetails) {
        
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        String email= customUserDetails.getEmail();

        List<PlaylistDTO> savedPlaylists = findPlaylistService.findPlaylistByUserId(email);

        List<PlaylistResponse> response = savedPlaylists.stream()
                    .map(savedPlaylist -> 
                    PlaylistResponse.builder()
                    .playlistId(savedPlaylist.getPlaylistId())
                    .playlistName(savedPlaylist.getPlaylistName())
                    .createdDate(savedPlaylist.getCreatedDate())
                    .createdBy(savedPlaylist.getCreatedBy())
                    .build())
                    .toList();
        

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
