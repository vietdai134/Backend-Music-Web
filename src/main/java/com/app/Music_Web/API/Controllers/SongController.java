package com.app.Music_Web.API.Controllers;

import com.app.Music_Web.API.Request.SongRequest;
import com.app.Music_Web.API.Response.SongResponse;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.Ports.In.GoogleDrive.GoogleDriveService;
import com.app.Music_Web.Application.Ports.In.Song.DeleteSongService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongService;
import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;
import com.app.Music_Web.Application.Ports.In.SongApproval.SaveSongApprovalService;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;
import com.app.Music_Web.Infrastructure.Persistence.CustomUserDetails;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final DeleteSongService deleteSongService;
    private final GoogleDriveService googleDriveService;
    private final SaveSongApprovalService saveSongApprovalService;
    public SongController(FindSongService findSongService, 
                            SaveSongService saveSongService,
                            DeleteSongService deleteSongService,
                            GoogleDriveService googleDriveService,
                            SaveSongApprovalService saveSongApprovalService) {
        this.saveSongService = saveSongService;
        this.findSongService = findSongService;
        this.deleteSongService=deleteSongService;
        this.googleDriveService=googleDriveService;
        this.saveSongApprovalService=saveSongApprovalService;
    }


    // Lấy danh sách file trên Google Drive
    @GetMapping("/files")
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
    public ResponseEntity<Void> createSong(@ModelAttribute SongRequest request,
                                            @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        String accessToken=googleDriveService.getAccessToken();
        byte[] songFileData= request.getSongFileData().getBytes();
        String fileId = googleDriveService.uploadFile(songFileData
                                        , request.getArtist()+"-"+request.getTitle()+".mp3", 
                                        accessToken);
        saveSongService.saveSong(request.getTitle(),
                                request.getArtist(),
                                request.getSongImage(),
                                fileId,
                                request.getGenreNames(),
                                request.isDownloadable());

        // Lưu vào song approval
        Long songId= findSongService.findByFileSongId(fileId);
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        Long userId=customUserDetails.getUserId();
        ApprovalStatus status = ApprovalStatus.valueOf("APPROVED".toUpperCase());
        saveSongApprovalService.saveSongApproval(songId, userId,status);

        
        return ResponseEntity.ok().build();
    }

    // Cập nhật tên file
    @PatchMapping("/files/{fileId}")
    public ResponseEntity<String> updateFileName(
            @PathVariable String fileId,
            @RequestParam("newName") String newName) throws IOException {
        String accessToken = googleDriveService.getAccessToken();
        String updatedFileInfo = googleDriveService.updateFileName(accessToken, fileId, newName);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updatedFileInfo);
    }

    // Xóa file
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileId) throws IOException {
        String accessToken = googleDriveService.getAccessToken();
        googleDriveService.deleteFile(accessToken, fileId);
        return ResponseEntity.noContent().build();
    }

    // Tải file về
    @GetMapping("/files/{fileId}/download")
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

    // @GetMapping("/all")
    // public Page<SongResponse> getAllSongs(@RequestParam(defaultValue = "0") int page,
    //                                      @RequestParam(defaultValue = "10") int size) {
    //     Pageable pageable = PageRequest.of(page, size,Sort.unsorted());
    //     Page<SongDTO> songs = findSongService.findAll(pageable);
    //     return songs.map(song-> SongResponse.builder()
    //                                         .id(song.getId())
    //                                         .title(song.getTitle())
    //                                         .build());
    // }

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
                .approvedDate(song.getApprovedDate())
                .downloadable(song.isDownloadable())
                .userName(song.getUserName())
                .build());
    }

    @GetMapping("/search")
    public ResponseEntity<?> getSongByTitle(@RequestParam String songTitle) {
        SongDTO song = findSongService.findBySongTitle(songTitle);
        if (song == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Song not found");
        }

        SongResponse response = SongResponse.builder()
                .songId(song.getSongId())
                .title(song.getTitle())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long songId){
        deleteSongService.deleteSong(songId);
        return ResponseEntity.noContent().build();
    }
}