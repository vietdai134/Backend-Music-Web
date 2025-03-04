package com.app.Music_Web.API.Controllers;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.GenreRequest;
import com.app.Music_Web.API.Response.GenreResponse;
import com.app.Music_Web.Application.DTO.GenreDTO;
import com.app.Music_Web.Application.Ports.In.Genre.FindGenreService;
import com.app.Music_Web.Application.Ports.In.Genre.SaveGenreService;
import com.app.Music_Web.Application.Ports.In.Genre.DeleteGenreService;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    private final FindGenreService findGenreService;
    private final SaveGenreService saveGenreService;
    private final DeleteGenreService deleteGenreService;
    public GenreController(FindGenreService findGenreService, 
                            SaveGenreService saveGenreService,
                            DeleteGenreService deleteGenreService){
        this.findGenreService=findGenreService;
        this.saveGenreService=saveGenreService;
        this.deleteGenreService=deleteGenreService;
        
    }

    @GetMapping("/all")
    public ResponseEntity<Page<GenreResponse>> getAllGenres(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.unsorted());
        Page<GenreDTO> genres = findGenreService.findAll(pageable);
        // return genres.map(genre-> GenreResponse.builder()
        //                                     .genreId(genre.getGenreId())
        //                                     .genreName(genre.getGenreName())
        //                                     .build());
        Page<GenreResponse> genreResponses = genres.map(genre -> 
            GenreResponse.builder()
                         .genreId(genre.getGenreId())
                         .genreName(genre.getGenreName())
                         .build()
        );

        return ResponseEntity.ok(genreResponses);
    }

    // @GetMapping("/search")
    // public GenreResponse getGenreByName(@RequestParam String genreName){
    //     GenreDTO genre = findGenreService.findByGenreName(genreName);
    //     return GenreResponse.builder()
    //             .genreId(genre.getGenreId())
    //             .genreName(genre.getGenreName())
    //             .build();
    // }

    @GetMapping("/search")
    public ResponseEntity<?> getGenreByName(@RequestParam String genreName) {
        GenreDTO genre = findGenreService.findByGenreName(genreName);
        if (genre == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Genre not found");
        }

        GenreResponse response = GenreResponse.builder()
                .genreId(genre.getGenreId())
                .genreName(genre.getGenreName())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long genreId){
        deleteGenreService.deleteGenre(genreId);
        return ResponseEntity.noContent().build();
    }

    // @PostMapping
    // public GenreResponse createGenre(@RequestBody GenreRequest request) {
    //     GenreDTO savedGenre = saveGenreService.saveGenre(request.getGenreName());
    //     return GenreResponse.builder()
    //             .genreId(savedGenre.getGenreId())
    //             .genreName(savedGenre.getGenreName())
    //             .build();
    // }
    @PostMapping
    public ResponseEntity<GenreResponse> createGenre(@RequestBody GenreRequest request) {
        GenreDTO savedGenre = saveGenreService.saveGenre(request.getGenreName());

        GenreResponse response = GenreResponse.builder()
                .genreId(savedGenre.getGenreId())
                .genreName(savedGenre.getGenreName())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
