package com.app.Music_Web.API.Controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.Application.DTO.LikedSongDTO;
import com.app.Music_Web.Application.Ports.In.LikedSong.DeleteLikedSongService;
import com.app.Music_Web.Application.Ports.In.LikedSong.FindLikedSongService;
import com.app.Music_Web.Application.Ports.In.LikedSong.SaveLikedSongService;
import com.app.Music_Web.Infrastructure.Persistence.CustomUserDetails;

@RestController
@RequestMapping("/api/liked-song")
public class LikedSongController {
    private final SaveLikedSongService saveLikedSongService;
    private final DeleteLikedSongService deleteLikedSongService;
    private final FindLikedSongService findLikedSongService;
    public LikedSongController(SaveLikedSongService saveLikedSongService,
                                DeleteLikedSongService deleteLikedSongService,
                                FindLikedSongService findLikedSongService) {
        this.saveLikedSongService = saveLikedSongService;
        this.deleteLikedSongService = deleteLikedSongService;
        this.findLikedSongService = findLikedSongService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('LIKE_SONG')")
    public ResponseEntity<Void> saveLikedSong(@RequestParam Long songId, 
                                @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        if (userDetails == null) {
            // Trả về lỗi 401 Unauthorized hoặc 400 Bad Request tùy ý
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email= customUserDetails.getEmail();
        saveLikedSongService.saveLikedSong(songId, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('LIKE_SONG')")
    public ResponseEntity<Void> deleteLikedSong(@RequestParam Long songId, 
                                @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        if (userDetails == null) {
            // Trả về lỗi 401 Unauthorized hoặc 400 Bad Request tùy ý
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email= customUserDetails.getEmail();
        deleteLikedSongService.deleteLikedSong(songId, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('LIKE_SONG')")
    public ResponseEntity<List<LikedSongDTO>> getLikedSong(
        @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        if (userDetails == null) {
            // Trả về lỗi 401 Unauthorized hoặc 400 Bad Request tùy ý
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email= customUserDetails.getEmail();
        List<LikedSongDTO> likedSongs = findLikedSongService.findLikedSongsByUser(email);
        return ResponseEntity.ok(likedSongs);
    }
}
