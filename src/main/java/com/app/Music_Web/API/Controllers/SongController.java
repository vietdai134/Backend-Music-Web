package com.app.Music_Web.API.Controllers;

import com.app.Music_Web.API.Request.SongRequest;
import com.app.Music_Web.API.Request.SongRequest.SongUpdateRequest;
import com.app.Music_Web.API.Response.SongResponse;
import com.app.Music_Web.Application.DTO.GenreDTO;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.Ports.In.Genre.FindGenreService;
import com.app.Music_Web.Application.Ports.In.GoogleDrive.GoogleDriveService;
import com.app.Music_Web.Application.Ports.In.Song.DeleteSongService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongService;
import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;
import com.app.Music_Web.Application.Ports.In.Song.UpdateSongService;
import com.app.Music_Web.Application.Ports.In.SongApproval.SaveSongApprovalService;
import com.app.Music_Web.Application.Ports.In.SongUpload.SaveSongUploadService;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;
import com.app.Music_Web.Infrastructure.Persistence.CustomUserDetails;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    private final FindSongService findSongService;
    private final SaveSongService saveSongService;
    // private final DeleteSongService deleteSongService;
    private final UpdateSongService updateSongService;
    private final GoogleDriveService googleDriveService;
    private final SaveSongApprovalService saveSongApprovalService;
    private final SaveSongUploadService saveSongUploadService;
    private final FindGenreService findGenreService;

    public SongController(FindSongService findSongService, 
                            SaveSongService saveSongService,
                            DeleteSongService deleteSongService,
                            UpdateSongService updateSongService,
                            GoogleDriveService googleDriveService,
                            SaveSongApprovalService saveSongApprovalService,
                            SaveSongUploadService saveSongUploadService,
                            FindGenreService findGenreService) {
        this.saveSongService = saveSongService;
        this.findSongService = findSongService;
        // this.deleteSongService=deleteSongService;
        this.updateSongService=updateSongService;
        this.googleDriveService=googleDriveService;
        this.saveSongApprovalService=saveSongApprovalService;
        this.saveSongUploadService=saveSongUploadService;
        this.findGenreService=findGenreService;
    }


    // Lấy danh sách file trên Google Drive
    @GetMapping("/files")
    @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    public ResponseEntity<String> listDriveFiles() throws IOException {
        String accessToken = googleDriveService.getAccessToken();
        String fileList = googleDriveService.listDriveFiles(accessToken);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fileList);
    }

    // Lấy thông tin file theo fileId
    @GetMapping("/files/{fileId}")
    public ResponseEntity<String> getFileInfo(@PathVariable String fileId) throws IOException {
        String accessToken = googleDriveService.getAccessToken();
        String fileInfo = googleDriveService.getFileInfo(accessToken, fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fileInfo);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    public ResponseEntity<Void> createSong(@ModelAttribute SongRequest request,
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
                            request.getGenreNames(),
                            request.isDownloadable()
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
            ApprovalStatus status = ApprovalStatus.APPROVED;
    
            CompletableFuture<Void> saveApprovalFuture = CompletableFuture.runAsync(() -> 
                saveSongApprovalService.saveSongApproval(songId, userId, status)
            );
    
            CompletableFuture<Void> saveUploadFuture = CompletableFuture.runAsync(() -> 
                saveSongUploadService.saveSongUpload(songId, userId)
            );
    
            // Đợi cả hai tác vụ hoàn tất
            CompletableFuture.allOf(saveApprovalFuture, saveUploadFuture).join();
        });
    
        // Đợi tất cả hoàn thành trước khi trả về response
        finalFuture.join();
    
        return ResponseEntity.ok().build();
    }
    

    // Tải file về
    @GetMapping("/download/{fileId}")
    @PreAuthorize("hasAuthority('DOWNLOAD_SONG')")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileId) throws IOException {
        String accessToken = googleDriveService.getAccessToken();
        byte[] fileData = googleDriveService.downloadFile(accessToken, fileId);

        // Lấy tên file từ Google Drive
        String fileName = googleDriveService.getFileName(accessToken, fileId);

        ByteArrayResource resource = new ByteArrayResource(fileData);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".mp3");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(fileData.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/all")
    public Page<SongResponse> getAllSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "APPROVED") String approvalStatus) { // Thêm tham số approvalStatus
        // Tạo Pageable với phân trang, mặc định không sắp xếp
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        // Chuyển đổi String thành ApprovalStatus
        ApprovalStatus status = ApprovalStatus.valueOf(approvalStatus.toUpperCase());

        // Gọi findAllWithStatus thay vì findAll
        Page<SongDTO> songs = findSongService.findAllWithStatus(status, pageable);

        // Ánh xạ sang SongResponse
        return songs.map(song -> SongResponse.builder()
                .songId(song.getSongId())
                .title(song.getTitle())
                .artist(song.getArtist())
                .fileSongId(song.getFileSongId())
                .songImage(song.getSongImage())
                .genres(song.getGenres())
                .approvedDate(song.getApprovedDate())
                .downloadable(song.isDownloadable())
                .userName(song.getUserName())
                .build());
    }
    
    @GetMapping("/{songId}")
    public ResponseEntity<SongResponse> getSongById(@PathVariable Long songId){
        SongDTO song= findSongService.findBySongId(songId);
        List<GenreDTO> genres = findGenreService.findGenreBySongId(songId);
        SongResponse songResponse = SongResponse.builder()
                            .songId(songId)
                            .title(song.getTitle())
                            .artist(song.getArtist())
                            .fileSongId(song.getFileSongId())
                            .songImage(song.getSongImage())
                            .genres(genres)
                            .downloadable(song.isDownloadable())
                            .build();
        return ResponseEntity.ok(songResponse);
    }

    @GetMapping("/search")
    public Page<SongResponse> searchAllSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "APPROVED") String approvalStatus) { // Thêm tham số approvalStatus
        // Tạo Pageable với phân trang, mặc định không sắp xếp
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        // Chuyển đổi String thành ApprovalStatus
        ApprovalStatus status = ApprovalStatus.valueOf(approvalStatus.toUpperCase());

        // Gọi findAllWithStatus thay vì findAll
        Page<SongDTO> songs = findSongService.searchByTitleOrArtist(keyword,status, pageable);

        // Ánh xạ sang SongResponse
        return songs.map(song -> SongResponse.builder()
                .songId(song.getSongId())
                .title(song.getTitle())
                .artist(song.getArtist())
                .fileSongId(song.getFileSongId())
                .songImage(song.getSongImage())
                .genres(song.getGenres())
                .approvedDate(song.getApprovedDate())
                .downloadable(song.isDownloadable())
                .userName(song.getUserName())
                .build());
    }

    // @DeleteMapping("/{songId}")
    // @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    // public ResponseEntity<Void> deleteGenre(@PathVariable Long songId){
    //     deleteSongService.deleteSong(songId);
    //     return ResponseEntity.noContent().build();
    // }

    @PutMapping(value = "/update/{songId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('EDIT_SONG')")
    public ResponseEntity<Void> updateSong(
            @PathVariable Long songId,
            @ModelAttribute SongUpdateRequest request) throws Exception {
        System.out.println("Updating userId=" + songId + ", avatar=" + 
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
                request.getGenreNames(),
                request.isDownloadable());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stream/{songFileId}")
    public ResponseEntity<Resource> streamSong(@PathVariable String songFileId) throws Exception {
        String accessToken = googleDriveService.getAccessToken();
        InputStream fileStream = googleDriveService.getFileStream(accessToken, songFileId);

        InputStreamResource resource = new InputStreamResource(fileStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + songFileId + ".mp3\"")
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(resource);
    }

}