package com.app.Music_Web.API.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.Application.Ports.In.Redis.RedisDeleteService;
import com.app.Music_Web.Application.Ports.In.Redis.RedisSaveService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongService;
import com.app.Music_Web.Application.Ports.In.SongApproval.UpdateSongApprovalService;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

@RestController
@RequestMapping("/api/song-approval")
public class SongApprovalController {
    private final UpdateSongApprovalService updateSongApprovalService;
    private final RedisDeleteService redisDeleteService;
    private final RedisSaveService redisSaveService;
    private final FindSongService findSongService;
    public SongApprovalController(
        UpdateSongApprovalService updateSongApprovalService,
        RedisDeleteService redisDeleteService,
        RedisSaveService redisSaveService,
        FindSongService findSongService
    ){
        this.updateSongApprovalService=updateSongApprovalService;
        this.redisDeleteService=redisDeleteService;
        this.redisSaveService=redisSaveService;
        this.findSongService=findSongService;
    }

    @PutMapping("/{songId}")
    @PreAuthorize("hasAuthority('MODERATE_SONG')")
    public ResponseEntity<Void> changeStatusSong(
        @PathVariable Long songId,
        @RequestParam ApprovalStatus approvalStatus
    ){
        updateSongApprovalService.changeStatusSong(songId, approvalStatus);
        // redisDeleteService.deleteSong(songId);
        if(approvalStatus==ApprovalStatus.REVOKED){
            redisDeleteService.deleteSong(songId);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/upload-song/{uploadId}")
    @PreAuthorize("hasAuthority('MODERATE_SONG')")
    public ResponseEntity<Void> changeStatusUploadSong(
        @PathVariable Long uploadId,
        @RequestParam ApprovalStatus approvalStatus
    ){
        updateSongApprovalService.changeStatusUploadSong(uploadId, approvalStatus);
        if(approvalStatus==ApprovalStatus.APPROVED){
            Long songId=findSongService.findByUploadId(uploadId);
            redisSaveService.syncAddSong(songId);
        }
        return ResponseEntity.ok().build();
    }
}
