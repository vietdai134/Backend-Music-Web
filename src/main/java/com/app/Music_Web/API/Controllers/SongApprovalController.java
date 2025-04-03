package com.app.Music_Web.API.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.Application.Ports.In.SongApproval.UpdateSongApprovalService;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

@RestController
@RequestMapping("/api/song-approval")
public class SongApprovalController {
    private final UpdateSongApprovalService updateSongApprovalService;

    public SongApprovalController(
        UpdateSongApprovalService updateSongApprovalService
    ){
        this.updateSongApprovalService=updateSongApprovalService;
    }

    @PutMapping("/{songId}")
    @PreAuthorize("hasAuthority('MODERATE_SONG')")
    public ResponseEntity<Void> changeStatusSong(
        @PathVariable Long songId,
        @RequestParam ApprovalStatus approvalStatus
    ){
        updateSongApprovalService.changeStatusSong(songId, approvalStatus);
        return ResponseEntity.ok().build();
    }

    @PutMapping("upload-song/{uploadId}")
    @PreAuthorize("hasAuthority('MODERATE_SONG')")
    public ResponseEntity<Void> changeStatusUploadSong(
        @PathVariable Long uploadId,
        @RequestParam ApprovalStatus approvalStatus
    ){
        updateSongApprovalService.changeStatusUploadSong(uploadId, approvalStatus);
        return ResponseEntity.ok().build();
    }
}
