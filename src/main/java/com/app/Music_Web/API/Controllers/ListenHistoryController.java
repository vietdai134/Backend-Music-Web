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

import com.app.Music_Web.Application.DTO.ListenHistoryDTO;
import com.app.Music_Web.Application.Ports.In.ListenHistory.DeleteListenHistoryService;
import com.app.Music_Web.Application.Ports.In.ListenHistory.FindListenHistoryService;
import com.app.Music_Web.Application.Ports.In.ListenHistory.SaveListenHistoryService;
import com.app.Music_Web.Infrastructure.Persistence.CustomUserDetails;

@RestController
@RequestMapping("/api/listen-history")
public class ListenHistoryController {
    private final SaveListenHistoryService saveListenHistoryService;
    private final DeleteListenHistoryService deleteListenHistoryService;
    private final FindListenHistoryService findListenHistoryService;
    public ListenHistoryController(SaveListenHistoryService saveListenHistoryService,
                                    DeleteListenHistoryService deleteListenHistoryService,
                                    FindListenHistoryService findListenHistoryService) {
        this.findListenHistoryService = findListenHistoryService;
        this.deleteListenHistoryService=deleteListenHistoryService;
        this.saveListenHistoryService = saveListenHistoryService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('HISTORY')")
    public ResponseEntity<Void> saveListenHistory(@RequestParam Long songId, 
                                @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        if (userDetails == null) {
            // Trả về lỗi 401 Unauthorized hoặc 400 Bad Request tùy ý
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email= customUserDetails.getEmail();
        saveListenHistoryService.saveListenHistory(songId, email);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('HISTORY')")
    public ResponseEntity<Void> deleteSongFromListenHistory(@RequestParam Long songId, 
                                @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        if (userDetails == null) {
            // Trả về lỗi 401 Unauthorized hoặc 400 Bad Request tùy ý
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email= customUserDetails.getEmail();
        deleteListenHistoryService.deleteSongFromListenHistory(songId, email);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('HISTORY')")
    public ResponseEntity<List<ListenHistoryDTO>> getListenHistory(
        @AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        if (userDetails == null) {
            // Trả về lỗi 401 Unauthorized hoặc 400 Bad Request tùy ý
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email= customUserDetails.getEmail();
        List<ListenHistoryDTO> listenHistory = findListenHistoryService.findListenHistoryByUser(email);
        return ResponseEntity.ok(listenHistory);
    }
}
