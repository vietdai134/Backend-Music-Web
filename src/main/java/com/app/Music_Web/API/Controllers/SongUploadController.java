package com.app.Music_Web.API.Controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Response.SongUploadResponse;
import com.app.Music_Web.Application.DTO.SongUploadDTO;
import com.app.Music_Web.Application.Ports.In.SongUpload.FindSongUploadService;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

@RestController
@RequestMapping("/api/song-upload")
public class SongUploadController {
    private final FindSongUploadService findSongUploadService;

    public SongUploadController (
        FindSongUploadService findSongUploadService
    ){
        this.findSongUploadService=findSongUploadService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('MODERATE_SONG')")
    public Page<SongUploadResponse> getAllUploadSongsWithStatus(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "PENDING") String approvalStatus
    ){
        Pageable pageable=PageRequest.of(page, size,Sort.unsorted());

        ApprovalStatus status = ApprovalStatus.valueOf(approvalStatus.toUpperCase());

        Page<SongUploadDTO> songsUpload= findSongUploadService.findAllWithSong(status, pageable);

        return songsUpload.map(song -> SongUploadResponse.builder()
        .uploadId(song.getUploadId())
        .uploadDate(song.getUploadDate())
        .songDto(song.getSongDto())
        .userName(song.getUserName())
        .build());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('MODERATE_SONG')")
    public Page<SongUploadResponse> searchAllUploadSongsWithStatus(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam String keyword,
        @RequestParam(defaultValue = "PENDING") String approvalStatus
    ){
        Pageable pageable=PageRequest.of(page, size,Sort.unsorted());

        ApprovalStatus status = ApprovalStatus.valueOf(approvalStatus.toUpperCase());

        Page<SongUploadDTO> songsUpload= findSongUploadService.searchUploadByTitleOrArtist(keyword,status, pageable);

        return songsUpload.map(song -> SongUploadResponse.builder()
        .uploadId(song.getUploadId())
        .uploadDate(song.getUploadDate())
        .songDto(song.getSongDto())
        .userName(song.getUserName())
        .build());
    }
}
