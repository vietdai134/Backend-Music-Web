package com.app.Music_Web.API.Controllers;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.SongRequest;
import com.app.Music_Web.API.Request.SongRequest.SongUpdateRequest;
import com.app.Music_Web.API.Response.PublicSongResponse;
import com.app.Music_Web.API.Response.SongUploadResponse;
import com.app.Music_Web.Application.DTO.SongRedisDTO;
import com.app.Music_Web.Application.DTO.SongUploadDTO;
import com.app.Music_Web.Application.Ports.In.GoogleDrive.GoogleDriveService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongService;
import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;
import com.app.Music_Web.Application.Ports.In.Song.UpdateSongService;
import com.app.Music_Web.Application.Ports.In.SongApproval.SaveSongApprovalService;
import com.app.Music_Web.Application.Ports.In.SongUpload.FindSongUploadService;
import com.app.Music_Web.Application.Ports.In.SongUpload.SaveSongUploadService;
import com.app.Music_Web.Application.Ports.In.WebSocket.SendStatusService;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;
import com.app.Music_Web.Infrastructure.Persistence.CustomUserDetails;

@RestController
@RequestMapping("/api/song-upload")
public class SongUploadController {
    private final FindSongUploadService findSongUploadService;
    private final GoogleDriveService googleDriveService;
     private final FindSongService findSongService;
    private final SaveSongService saveSongService;
    private final SaveSongApprovalService saveSongApprovalService;
    private final SaveSongUploadService saveSongUploadService;
    private final UpdateSongService updateSongService;
    private final SendStatusService sendStatusService;
    public SongUploadController (
        FindSongService findSongService,
        FindSongUploadService findSongUploadService,
        GoogleDriveService googleDriveService,
        SaveSongService saveSongService,
        SaveSongApprovalService saveSongApprovalService,
        SaveSongUploadService saveSongUploadService,
        UpdateSongService updateSongService,
        SendStatusService sendStatusService
    ){
        this.findSongUploadService=findSongUploadService;
        this.googleDriveService=googleDriveService;
        this.saveSongService=saveSongService;
        this.saveSongApprovalService=saveSongApprovalService;
        this.saveSongUploadService=saveSongUploadService;
        this.findSongService=findSongService;
        this.updateSongService=updateSongService;
        this.sendStatusService=sendStatusService;

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


    @GetMapping("/all-song-upload")
    @PreAuthorize("hasAuthority('UPLOAD_SONG')")
    public ResponseEntity<Page<PublicSongResponse>> getAllSongUploadWithApproveStatus(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "uploadDate,desc") String sort,
        @RequestParam(defaultValue = "PENDING") String approvalStatus, 
        @AuthenticationPrincipal UserDetails userDetails
    ){
        Sort sortOrder = Sort.unsorted(); // Mặc định không sắp xếp nếu không có tham số
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String field = sortParams[0]; // Ví dụ: "uploadDate"
            String direction = sortParams.length > 1 ? sortParams[1] : "asc"; // Mặc định "asc" nếu không có hướng
            sortOrder = Sort.by(Sort.Direction.fromString(direction), field);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        if (userDetails == null) {
            // Trả về lỗi 401 Unauthorized hoặc 400 Bad Request tùy ý
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email= customUserDetails.getEmail();
        ApprovalStatus status = ApprovalStatus.valueOf(approvalStatus.toUpperCase());
        Page<SongRedisDTO> songsUpload= findSongUploadService.findAllSongUploadWithApproveStatus(status,email, pageable);
        Page<PublicSongResponse> songResponse = songsUpload
        .map(song -> PublicSongResponse.builder()
                .songId(song.getSongId())
                .title(song.getTitle())
                .artist(song.getArtist())
                .songImage(song.getSongImage())
                .uploadDate(song.getUploadDate())
                .fileSongId(song.getFileSongId())
                .albumNames(song.getAlbumNames())
                .build());
        return ResponseEntity.ok(songResponse);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('UPLOAD_SONG')")
    public ResponseEntity<Void> uploadSong(@ModelAttribute SongRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        String accessToken = googleDriveService.getAccessToken();
        byte[] songFileData = request.getSongFileData().getBytes();
        String fileName = request.getArtist() + "-" + request.getTitle() + ".mp3";
    
        // Bước 1: Upload file lên Google Drive
        CompletableFuture<String> uploadFileFuture = CompletableFuture.supplyAsync(() -> 
            googleDriveService.uploadFile(songFileData, fileName, accessToken)
        );
    
        // Bước 2: Lưu bài hát vào DB (chạy ngay sau khi có fileId)
        CompletableFuture<Long> saveSongAndFindIdFuture = uploadFileFuture.thenCompose(fileId -> {
            return CompletableFuture.runAsync(() -> 
                {
                    try {
                        saveSongService.saveSong(
                            request.getTitle(),
                            request.getArtist(),
                            request.getSongImage(),
                            fileId,
                            request.getGenreNames()
                            // request.isDownloadable()
                        );
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            ).thenApply(ignored -> findSongService.findByFileSongId(fileId)); // Sau khi lưu xong, lấy songId
        });
    
        // Bước 3: Khi có songId, chạy saveSongApproval & saveSongUpload song song
        CompletableFuture<Void> finalFuture = saveSongAndFindIdFuture.thenAccept(songId -> {
            CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
            Long userId = customUserDetails.getUserId();
            ApprovalStatus status = ApprovalStatus.PENDING;
    
            CompletableFuture<Void> saveApprovalFuture = CompletableFuture.runAsync(() -> 
                saveSongApprovalService.saveSongApproval(songId, userId, status)
            );
    
            CompletableFuture<Void> saveUploadFuture = CompletableFuture.runAsync(() -> 
                saveSongUploadService.saveSongUpload(songId, userId)
            );
    
            // Đợi cả hai tác vụ hoàn tất
            CompletableFuture.allOf(saveApprovalFuture, saveUploadFuture).join();
            sendStatusService.notifyStatusChange(songId, status.toString());
        });
    
        // Đợi tất cả hoàn thành trước khi trả về response
        finalFuture.join();
        
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/update/{songId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('UPLOAD_SONG')")
    public ResponseEntity<Void> updateUploadSong(
            @PathVariable Long songId,
            @ModelAttribute SongUpdateRequest request) throws Exception {
        System.out.println("Updating songId=" + songId + ", avatar=" + 
        (request.getSongImage() != null ? request.getSongImage().getOriginalFilename() : "null"));
        String accessToken = googleDriveService.getAccessToken();
        String fileName = request.getArtist() + "-" + request.getTitle() + ".mp3";
        googleDriveService.updateFileName(accessToken, request.getSongFileId(), fileName);

        updateSongService.updateSong(
                songId,
                request.getTitle(),
                request.getArtist(),
                request.getSongFileId(),
                request.getSongImage(),
                request.getGenreNames());

        return ResponseEntity.ok().build();
    }

}
