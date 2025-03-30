package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SongRepositoryPort {
    Page<Song> findAll(Pageable pageable); 
    Page<Object[]> findAllWithStatus(ApprovalStatus status, Pageable pageable);
    Page<Object[]> searchByTitleOrArtist(String keyword, ApprovalStatus status, Pageable pageable);

    Song save(Song song);
    void delete(Song song);
    Song findByTitle_Title(String songTitle);
    Song findByArtist_Artist(String songArtist);
    Song findBySongId(Long songId);

    Song findByFileSongId(String fileSongId);
}
