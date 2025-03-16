package com.app.Music_Web.API.Controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.AlbumRequest;
import com.app.Music_Web.API.Response.AlbumResponse;
import com.app.Music_Web.Application.DTO.AlbumDTO;
import com.app.Music_Web.Application.Ports.In.Album.FindAlbumService;
import com.app.Music_Web.Application.Ports.In.Album.SaveAlbumService;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {
    private final SaveAlbumService saveAlbumService;
    private final FindAlbumService findAlbumService;
    public AlbumController(SaveAlbumService saveAlbumService, FindAlbumService findAlbumService) {
        this.saveAlbumService = saveAlbumService;
        this.findAlbumService = findAlbumService;
    }

    @GetMapping("/{userId}")
    public Page<AlbumResponse> getAllSongs(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size, @PathVariable Long userId) {
        Pageable pageable = PageRequest.of(page, size,Sort.unsorted());
        Page<AlbumDTO> albums = findAlbumService.findByUser_UserId(pageable, userId);
        return albums.map(album-> AlbumResponse.builder()
                                            .albumId(album.getAlbumId())
                                            .albumName(album.getAlbumName())
                                            .albumImage(album.getAlbumImage())
                                            .build());
    }

    @PostMapping
    public AlbumResponse createAlbum(@RequestBody AlbumRequest request) {
        AlbumDTO savedAlbum = saveAlbumService.saveAlbum(request.getAlbumName(), request.getAlbumImage(), request.getUserId());
        return AlbumResponse.builder()
                .albumId(savedAlbum.getAlbumId())
                .albumName(savedAlbum.getAlbumName())
                .albumImage(savedAlbum.getAlbumImage())
                .build();
    }
}
