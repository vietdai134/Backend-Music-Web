package com.app.Music_Web.Application.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.app.Music_Web.Application.DTO.GenreDTO;
import com.app.Music_Web.Application.Mapper.GenreMapper;
import com.app.Music_Web.Application.Ports.In.Genre.DeleteGenreService;
import com.app.Music_Web.Application.Ports.In.Genre.FindGenreService;
import com.app.Music_Web.Application.Ports.In.Genre.SaveGenreService;
import com.app.Music_Web.Application.Ports.In.Genre.UpdateGenreService;
import com.app.Music_Web.Application.Ports.Out.GenreRepositoryPort;
import com.app.Music_Web.Domain.Entities.Genre;
import com.app.Music_Web.Domain.ValueObjects.Genre.GenreName;


@Service
public class GenreServiceImpl implements FindGenreService, SaveGenreService,DeleteGenreService, UpdateGenreService{
    private final GenreRepositoryPort genreRepositoryPort;
    public GenreServiceImpl(GenreRepositoryPort genreRepositoryPort){
        this.genreRepositoryPort=genreRepositoryPort;
    }
    
    @Override
    public GenreDTO saveGenre(String genreName) {
        Genre genre = Genre.builder()
                            .genreName(new GenreName(genreName))
                            .build();
        Genre saveGenre= genreRepositoryPort.save(genre);
        return GenreMapper.toDTO(saveGenre);
        
    }

    @Override
    public Page<GenreDTO> findAll(Pageable pageable) {
        Page<Genre> genres = genreRepositoryPort.findAll(pageable);
        return genres.map(GenreMapper::toDTO);
    }

    // @Override
    // public GenreDTO findByGenreName(String genreName) {
    //     Genre genre = genreRepositoryPort.findByGenreName_GenreName(genreName);
    //     if(genre==null){
    //         throw new RuntimeException("Genre not found");
    //     }
    //     return GenreMapper.toDTO(genre);
    // }
    
    @Override
    public void deleteGenre(Long genreId) {
        Genre genre = genreRepositoryPort.findByGenreId(genreId);
        if(genre==null){
            throw new RuntimeException("Genre not found");
        }
        genreRepositoryPort.delete(genre);            
    }

     @Override
    public GenreDTO updateGenre(Long genreId, String genreName) {
        // Tìm user theo userId
        Genre genre = genreRepositoryPort.findByGenreId(genreId);
        if(genre==null){
            throw new RuntimeException("Genre not found");
        }

        genre.setGenreName(new GenreName(genreName));

        // Lưu user đã cập nhật
        Genre updatedGenre = genreRepositoryPort.save(genre);
        return GenreMapper.toDTO(updatedGenre);
    }

    @Override
    public GenreDTO findByGenreId(Long genreId) {
        Genre genre = genreRepositoryPort.findByGenreId(genreId);
        if(genre==null){
            throw new RuntimeException("Genre not found");
        }
        return GenreMapper.toDTO(genre);
    }

    @Override
    public Page<GenreDTO> searchByGenreName(String keyword, Pageable pageable) {
        Page<Genre> genre = genreRepositoryPort.searchByGenreName(keyword, pageable);
        return genre.map(GenreMapper::toDTO);
    }
    
}
