package com.app.Music_Web.API.Controllers;

// import com.app.Music_Web.API.Request.SongRequest;
import com.app.Music_Web.API.Response.SongResponse;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.Ports.In.GoogleDrive.GoogleDriveService;
import com.app.Music_Web.Application.Ports.In.Song.DeleteSongService;
import com.app.Music_Web.Application.Ports.In.Song.FindSongService;
import com.app.Music_Web.Application.Ports.In.Song.SaveSongService;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    private final FindSongService findSongService;
    private final SaveSongService saveSongService;
    private final DeleteSongService deleteSongService;
    private final GoogleDriveService googleDriveService;
    public SongController(FindSongService findSongService, 
                            SaveSongService saveSongService,
                            DeleteSongService deleteSongService,
                            GoogleDriveService googleDriveService) {
        this.saveSongService = saveSongService;
        this.findSongService = findSongService;
        this.deleteSongService=deleteSongService;
        this.googleDriveService=googleDriveService;
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

    @PostMapping(consumes = "multipart/form-data") // Dùng multipart/form-data để hỗ trợ file
    public SongResponse createSong(
            @RequestPart("title") String title,              // Tiêu đề bài hát (text)
            @RequestPart("artist") String artist,            // Nghệ sĩ (text)
            @RequestPart("song_image") String song_image,    // Đường dẫn ảnh (text)
            @RequestPart("songFile") MultipartFile songFile, // File nhạc (chọn file)
            @RequestPart("downloadable") String downloadable)// Có thể tải xuống (text)
            throws IOException {
        
        String accessToken = googleDriveService.getAccessToken();

        // Upload file nhạc lên Google Drive
        byte[] songFileData = songFile.getBytes();
        String fileId = googleDriveService.uploadFile(songFileData, title + ".mp3", accessToken);

        // Lưu thông tin bài hát
        SongDTO savedSong = saveSongService.saveSong(
                title,
                artist,
                song_image, // Giữ nguyên dạng String như trước
                fileId,
                Boolean.parseBoolean(downloadable)); // Chuyển String thành boolean

        return SongResponse.builder()
                .id(savedSong.getId())
                .title(savedSong.getTitle())
                .build();
    }

    // @PostMapping(consumes = "multipart/form-data")
    // public SongResponse createSong(@RequestBody SongRequest request) throws IOException{
    //     String accessToken=googleDriveService.getAccessToken();

    //     String fileId = googleDriveService.uploadFile(request.getSongFileData(), request.getTitle() + ".mp3", accessToken);

    //     SongDTO savedSong = saveSongService.saveSong(
    //                             request.getTitle(), 
    //                             request.getArtist(), 
    //                             request.getSong_image(),
    //                             fileId,
    //                             request.isDownloadable());

    //     return SongResponse.builder()
    //             .id(savedSong.getId())
    //             .title(savedSong.getTitle())
    //             .build();
    // }

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

        ByteArrayResource resource = new ByteArrayResource(fileData);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileId + ".mp3");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(fileData.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/all")
    public Page<SongResponse> getAllSongs(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.unsorted());
        Page<SongDTO> songs = findSongService.findAll(pageable);
        return songs.map(song-> SongResponse.builder()
                                            .id(song.getId())
                                            .title(song.getTitle())
                                            .build());
    }


    @GetMapping("/search")
    public ResponseEntity<?> getSongByTitle(@RequestParam String songTitle) {
        SongDTO song = findSongService.findBySongTitle(songTitle);
        if (song == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Song not found");
        }

        SongResponse response = SongResponse.builder()
                .id(song.getId())
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