package com.app.Music_Web.API.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.AlbumRequest;
import com.app.Music_Web.API.Response.AlbumResponse;
import com.app.Music_Web.API.Response.AlbumSongResponse;
import com.app.Music_Web.Application.DTO.AlbumDTO;
import com.app.Music_Web.Application.DTO.AlbumSongDTO;
import com.app.Music_Web.Application.Ports.In.Album.DeleteAlbumService;
import com.app.Music_Web.Application.Ports.In.Album.FindAlbumService;
import com.app.Music_Web.Application.Ports.In.Album.SaveAlbumService;
import com.app.Music_Web.Application.Ports.In.Album.UpdateAlbumService;
import com.app.Music_Web.Application.Ports.In.Redis.RedisUpdateService;
import com.app.Music_Web.Infrastructure.Persistence.CustomUserDetails;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    private final SaveAlbumService saveAlbumService;
    private final FindAlbumService findAlbumService;
    private final DeleteAlbumService deleteAlbumService;
    private final UpdateAlbumService updateAlbumService;
    private final RedisUpdateService redisUpdateService;
    public AlbumController(
        SaveAlbumService saveAlbumService, 
        FindAlbumService findAlbumService,
        DeleteAlbumService deleteAlbumService,
        UpdateAlbumService updateAlbumService,
        RedisUpdateService redisUpdateService) {
        this.saveAlbumService = saveAlbumService;
        this.findAlbumService = findAlbumService;
        this.deleteAlbumService = deleteAlbumService;
        this.updateAlbumService = updateAlbumService;
        this.redisUpdateService=redisUpdateService;
    }

    @PostMapping("/create")
    public AlbumResponse createAlbum(
        @ModelAttribute AlbumRequest request,
        @AuthenticationPrincipal UserDetails userDetails) {
            CustomUserDetails customUserDetails= (CustomUserDetails) userDetails;
            Long userId = customUserDetails.getUserId();
        AlbumDTO savedAlbum = saveAlbumService.saveAlbum(
            request.getAlbumName(), 
            request.getAlbumImage(), 
            userId
            );
        return AlbumResponse.builder()
                .albumId(savedAlbum.getAlbumId())
                .albumName(savedAlbum.getAlbumName())
                .albumImage(savedAlbum.getAlbumImage())
                .createdDate(savedAlbum.getCreatedDate())
                .build();
    }

    @PostMapping("/add-song")
    public ResponseEntity<Void> addSongToAlbum(
        @RequestParam Long songId,
        @RequestParam Long albumId
    ){
        saveAlbumService.saveSongToAlbum(songId,albumId);
        redisUpdateService.syncUpdateSong(songId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-multiple-song")
    public ResponseEntity<Void> addMultiSongToAlbum(
        @RequestParam List<Long> songIds,
        @RequestParam Long albumId
    ){
        saveAlbumService.saveMultipleSongsToAlbum(songIds, albumId);
        for ( Long songId : songIds){
            redisUpdateService.syncUpdateSong(songId);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponse>> getListAlbum(
        @AuthenticationPrincipal UserDetails userDetails
    ){
        CustomUserDetails customUserDetails= (CustomUserDetails) userDetails;
        Long userId = customUserDetails.getUserId();
        List<AlbumDTO> albums = findAlbumService.findAlbumByUserId(userId);
        List<AlbumResponse> albumResponses = albums.stream()
            .map(album -> AlbumResponse.builder()
                .albumId(album.getAlbumId())
                .albumName(album.getAlbumName())
                .albumImage(album.getAlbumImage())
                .createdDate(album.getCreatedDate())
                .build())
            .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(albumResponses);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAlbum(
        @RequestParam Long albumId
    ){
        deleteAlbumService.deleteAlbum(albumId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete-song")
    public ResponseEntity<Void> deleteSongFromAlbum(
        @RequestParam Long songId,
        @RequestParam Long albumId
    ){
        deleteAlbumService.deleteSongFromAlbum(songId, albumId);
        redisUpdateService.syncUpdateSong(songId);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateAlbum(
        @RequestParam Long albumId,
        @ModelAttribute AlbumRequest request,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        CustomUserDetails customUserDetails= (CustomUserDetails) userDetails;
        Long userId = customUserDetails.getUserId();
        updateAlbumService.updateAlbum(albumId, request.getAlbumName(), request.getAlbumImage(), userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/songs")
    public ResponseEntity<List<AlbumSongResponse>> getListSongInAlbum(
        @RequestParam Long albumId
    ){
        List<AlbumSongDTO> savedAlbums= findAlbumService.findSongIdsByAlbumId(albumId);
        List<AlbumSongResponse> responses= savedAlbums.stream()
        .map(savedAlbum -> AlbumSongResponse.builder()
                .albumSongId(savedAlbum.getAlbumSongId())
                .songId(savedAlbum.getSongId())
                .albumId(savedAlbum.getAlbumId())
                .build())
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);

    }
}
