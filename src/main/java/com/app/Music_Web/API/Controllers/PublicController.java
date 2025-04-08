package com.app.Music_Web.API.Controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
// import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Response.PublicSongResponse;
import com.app.Music_Web.Application.DTO.SongRedisDTO;
import com.app.Music_Web.Application.Ports.In.Public.FindService;
import com.app.Music_Web.Application.Ports.In.Redis.RedisSearchService;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    private final FindService findService;
    private final RedisSearchService redisSearchService;
    public PublicController(FindService findService, RedisSearchService redisSearchService) {
        this.redisSearchService = redisSearchService;
        this.findService = findService;
    }
    

    @GetMapping("/all")
    public Page<PublicSongResponse> getAllSongs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "uploadDate,desc") String sort) { 
        
        Sort sortOrder = Sort.unsorted(); // Mặc định không sắp xếp nếu không có tham số
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String field = sortParams[0]; // Ví dụ: "uploadDate"
            String direction = sortParams.length > 1 ? sortParams[1] : "asc"; // Mặc định "asc" nếu không có hướng
            sortOrder = Sort.by(Sort.Direction.fromString(direction), field);
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        // Gọi findAllWithStatus thay vì findAll
        Page<SongRedisDTO> songs = findService.findAllSong( pageable);

        // Ánh xạ sang SongResponse
        return songs.map(song -> PublicSongResponse.builder()
                .songId(song.getSongId())
                .title(song.getTitle())
                .artist(song.getArtist())
                .songImage(song.getSongImage())
                .fileSongId(song.getFileSongId())
                // .genres(song.getGenres())
                .genresName(song.getGenresName())
                .uploadDate(song.getUploadDate())
                .downloadable(song.isDownloadable())
                .userName(song.getUserName())
                .build());
    }


    // @GetMapping("/songs/search")
    // public List<SongRedisDTO> search(
    //         @RequestParam(required = false) String title,
    //         @RequestParam(required = false) String artist,
    //         @RequestParam(required = false) List<String> genres,
    //         @RequestParam(required = false) String username,
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size,
    //         @RequestParam(defaultValue = "uploadDate") String sortBy,
    //         @RequestParam(defaultValue = "desc") String sortDirection) {

    //     int offset = page * size;
    //     return redisSearchService.searchSongs(title, artist, genres, username,
    //                                     offset, size, sortBy, sortDirection);
    // }

    @GetMapping("/songs/search")
    public Page<SongRedisDTO> search(
            @RequestParam(required = false) List<String> songIds,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) List<String> genres,
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "uploadDate,desc") String sort) { // Thay 4 tham số bằng Pageable

        Sort sortOrder = Sort.unsorted(); // Mặc định không sắp xếp nếu không có tham số
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String field = sortParams[0]; // Ví dụ: "uploadDate"
            String direction = sortParams.length > 1 ? sortParams[1] : "asc"; // Mặc định "asc" nếu không có hướng
            sortOrder = Sort.by(Sort.Direction.fromString(direction), field);
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        return redisSearchService.searchSongs(songIds,title, artist, genres, username, pageable);
    }
}
