package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SongRepository extends JpaRepository<Song, Long>, SongRepositoryPort {
    @Override
    @Query("SELECT s.songId, s.title.title, s.artist.artist, "+
            "s.songImage, s.fileSongId, s.downloadable, "+
            "sa.approvedDate, u.userName.userName FROM Song s " +
           "LEFT JOIN s.songApprovals sa " +
           "LEFT JOIN sa.user u " +
           "WHERE sa.approvalStatus = :status")
    Page<Object[]> findAllWithStatus(@Param("status") ApprovalStatus status, Pageable pageable);

    @Override
    @Query("SELECT s.songId, s.title.title, s.artist.artist, "+
            "s.songImage, s.fileSongId, s.downloadable, "+
            "sa.approvedDate, u.userName.userName FROM Song s " +
           "LEFT JOIN s.songApprovals sa " +
           "LEFT JOIN sa.user u " +
           "WHERE sa.approvalStatus = :status "+
           "AND (LOWER(s.title.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "+
           "OR LOWER(s.artist.artist) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Object[]> searchByTitleOrArtist(@Param("keyword") String keyword,
                @Param("status") ApprovalStatus status, Pageable pageable);

}
