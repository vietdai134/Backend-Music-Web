package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.Music_Web.Application.Ports.Out.SongUploadRepositoryPort;
import com.app.Music_Web.Domain.Entities.SongUpload;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

public interface SongUploadRepository extends JpaRepository<SongUpload,Long>,SongUploadRepositoryPort{
    @Override
    @Query("SELECT su.uploadId, su.uploadDate, s.title.title, "+
            "s.artist.artist, s.songImage, s.fileSongId, "+
            "u.userName.userName FROM SongUpload su " +
           "LEFT JOIN su.song s " +
           "LEFT JOIN su.user u " +
           "INNER JOIN s.songApprovals sa " +
           "WHERE sa.approvalStatus = :status "+
           "ORDER BY su.uploadDate DESC")
    Page<Object[]> findAllWithSong(@Param("status") ApprovalStatus status, Pageable pageable);

    @Override
    @Query("SELECT su.uploadId, su.uploadDate, s.title.title, "+
            "s.artist.artist, s.songImage, s.fileSongId, "+
            "u.userName.userName FROM SongUpload su " +
           "LEFT JOIN su.song s " +
           "LEFT JOIN su.user u " +
           "INNER JOIN s.songApprovals sa " +
           "WHERE sa.approvalStatus = :status "+
           "AND (LOWER(s.title.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "+
           "OR LOWER(s.artist.artist) LIKE LOWER(CONCAT('%', :keyword, '%'))) "+
           "ORDER BY su.uploadDate DESC")
    Page<Object[]> searchUploadByTitleOrArtist(@Param("keyword") String keyword,
        @Param("status") ApprovalStatus status, Pageable pageable);

}
