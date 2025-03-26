package com.app.Music_Web.API.Controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app.Music_Web.API.Request.GenreRequest;
import com.app.Music_Web.API.Response.GenreResponse;
import com.app.Music_Web.Application.DTO.GenreDTO;
import com.app.Music_Web.Application.Ports.In.Genre.FindGenreService;
import com.app.Music_Web.Application.Ports.In.Genre.SaveGenreService;
import com.app.Music_Web.Application.Ports.In.Genre.UpdateGenreService;
import com.app.Music_Web.Application.Ports.In.Genre.DeleteGenreService;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    private final FindGenreService findGenreService;
    private final SaveGenreService saveGenreService;
    private final DeleteGenreService deleteGenreService;
    private final UpdateGenreService updateGenreService;
    public GenreController(FindGenreService findGenreService, 
                            SaveGenreService saveGenreService,
                            DeleteGenreService deleteGenreService,
                            UpdateGenreService updateGenreService){
        this.findGenreService=findGenreService;
        this.saveGenreService=saveGenreService;
        this.deleteGenreService=deleteGenreService;
        this.updateGenreService=updateGenreService;
        
    }

    @GetMapping("/all")
    public ResponseEntity<Page<GenreResponse>> getAllGenres(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.unsorted());
        Page<GenreDTO> genres = findGenreService.findAll(pageable);
        Page<GenreResponse> genreResponses = genres.map(genre -> 
            GenreResponse.builder()
                         .genreId(genre.getGenreId())
                         .genreName(genre.getGenreName())
                         .build()
        );

        return ResponseEntity.ok(genreResponses);
    }

    @GetMapping("/list")
    public ResponseEntity<List<GenreResponse>> getListAllGenres() {
        List<GenreDTO> genres = findGenreService.findAll();
        List<GenreResponse> genreResponses =genres.stream()
                    .map(genre-> GenreResponse.builder()
                        .genreId(genre.getGenreId())
                        .genreName(genre.getGenreName())
                        .build())
                    .toList();
        return ResponseEntity.ok(genreResponses);
        
    }

    @GetMapping("/{genreId}")
    public ResponseEntity<GenreResponse> getGenreById(@PathVariable Long genreId){
        GenreDTO genre= findGenreService.findByGenreId(genreId);
        GenreResponse genreResponse= GenreResponse.builder()
                                    .genreId(genreId)
                                    .genreName(genre.getGenreName())
                                    .build();
        return ResponseEntity.ok(genreResponse);
    }

    // @GetMapping("/search")
    // public ResponseEntity<?> getGenreByName(@RequestParam String genreName) {
    //     GenreDTO genre = findGenreService.findByGenreName(genreName);
    //     if (genre == null) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Genre not found");
    //     }

    //     GenreResponse response = GenreResponse.builder()
    //             .genreId(genre.getGenreId())
    //             .genreName(genre.getGenreName())
    //             .build();

    //     return ResponseEntity.ok(response);
    // }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long genreId){
        deleteGenreService.deleteGenre(genreId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<GenreResponse> createGenre(@RequestBody GenreRequest request) {
        GenreDTO savedGenre = saveGenreService.saveGenre(request.getGenreName());

        GenreResponse response = GenreResponse.builder()
                .genreId(savedGenre.getGenreId())
                .genreName(savedGenre.getGenreName())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{genreId}")
    public ResponseEntity<Void> updateGenre(
            @PathVariable Long genreId,
            @RequestBody GenreRequest request) {
        updateGenreService.updateGenre(
                genreId,
                request.getGenreName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<GenreResponse>> searchGenresFuzzy(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        Page<GenreDTO> genres = findGenreService.searchByGenreName(keyword, pageable);
        Page<GenreResponse> genreResponse = genres.map(genre -> GenreResponse.builder()
                .genreId(genre.getGenreId())
                .genreName(genre.getGenreName())
                .build());
        return ResponseEntity.ok(genreResponse);
    }
}
