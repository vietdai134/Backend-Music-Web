package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.Music_Web.Application.Ports.Out.GenreRepositoryPort;
import com.app.Music_Web.Domain.Entities.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long>,GenreRepositoryPort{
    @Override
    @Query("SELECT g FROM Genre g " +
           "WHERE LOWER(g.genreName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Genre> searchByGenreName(@Param("keyword") String keyword,Pageable pageable);

    @Query("SELECT g FROM Genre g WHERE g.genreName.genreName IN :names")
    List<Genre> findByGenreNameIn(@Param("names") List<String> names);

    @Override
    @Query("SELECT g "+
            "FROM Genre g "+
            "LEFT JOIN g.songGenres sg "+
            "LEFT JOIN sg.song s "+
            "WHERE s.songId IN :songId")
    List<Genre> findGenresBySongId(@Param("songId") List<Long> songId);

    @Override
    @Query("SELECT g "+
            "FROM Genre g "+
            "LEFT JOIN g.songGenres sg "+
            "LEFT JOIN sg.song s "+
            "WHERE s.songId = :songId")
    List<Genre> findGenresBySongId(@Param("songId") Long songId);
}
