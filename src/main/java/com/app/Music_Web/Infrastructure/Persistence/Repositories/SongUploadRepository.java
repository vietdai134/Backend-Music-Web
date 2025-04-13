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


        @Override
        @Query(value ="SELECT s.song_id, s.title as title, s.artist as artist, "+
                "s.song_image, su.upload_date as uploadDate, s.file_song_id, "+
                "group_concat(a.album_name) albumNames "+
                "FROM song_upload su "+
                "INNER JOIN song s on s.song_id = su.song_id "+
                "INNER JOIN song_approval sa on sa.song_id = su.song_id "+
                "LEFT JOIN album_song als on als.song_id = su.song_id "+
                "LEFT JOIN album a on a.album_id = als.album_id "+
                "WHERE sa.approval_status= :approvalStatus "+
                "AND su.uploaded_by= :userId "+
                "GROUP BY s.song_id, s.title, s.artist, "+
                "s.song_image, su.upload_date, s.file_song_id ",nativeQuery = true)
        Page<Object[]> findAllSongUploadWithApproveStatus(
                @Param("approvalStatus") String approvalStatus,
                @Param("userId") Long userId,
                Pageable pageable);
        
}
