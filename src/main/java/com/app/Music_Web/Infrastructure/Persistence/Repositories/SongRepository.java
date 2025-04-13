package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import com.app.Music_Web.Application.Ports.Out.SongRepositoryPort;
import com.app.Music_Web.Domain.Entities.Song;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SongRepository extends JpaRepository<Song, Long>, SongRepositoryPort {
        @Override
        @Query("SELECT s.songId, s.title.title, s.artist.artist, "+
                "s.songImage, s.fileSongId, "+
                "sa.approvedDate, u.userName.userName FROM Song s " +
                "LEFT JOIN s.songApprovals sa " +
                "LEFT JOIN sa.user u " +
                "WHERE sa.approvalStatus = :status")
        Page<Object[]> findAllWithStatus(@Param("status") ApprovalStatus status, Pageable pageable);

        @Override
        @Query("SELECT s.songId, s.title.title, s.artist.artist, "+
                "s.songImage, s.fileSongId, "+
                "sa.approvedDate, u.userName.userName FROM Song s " +
                "LEFT JOIN s.songApprovals sa " +
                "LEFT JOIN sa.user u " +
                "WHERE sa.approvalStatus = :status "+
                "AND (LOWER(s.title.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "+
                "OR LOWER(s.artist.artist) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        Page<Object[]> searchByTitleOrArtist(@Param("keyword") String keyword,
                        @Param("status") ApprovalStatus status, Pageable pageable);


        @Override
        @Query(value = "select s.song_id, s.title as title, s.artist as artist, " + 
                "s.song_image, s.file_song_id, " + 
                "su.upload_date as uploadDate, u.user_name, "+
                "group_concat(distinct g.genre_name) genres, " + 
                "group_concat(distinct a.album_name) albumNames "+
                "from song s " + 
                "left join song_approval sa on s.song_id=sa.song_id " + 
                "left join song_upload su on s.song_id=su.song_id " + 
                "left join user u on su.uploaded_by=u.user_id " + 
                "left join song_genre sg on s.song_id=sg.song_id " + 
                "left join genre g on sg.genre_id=g.genre_id " + 
                "left join album_song als on als.song_id = s.song_id "+
                "left join album a on a.album_id = als.album_id "+
                "where sa.approval_status='APPROVED' " + 
                "group by  s.song_id, s.title,s.artist, " + 
                "s.song_image, s.file_song_id, " + 
                "su.upload_date , u.user_name "
                , nativeQuery = true)
        Page<Object[]> findAllSongWithApproved(Pageable pageable);

        @Override
        @Query(value = "select s.song_id, s.title as title, s.artist as artist, " + 
                "s.song_image, s.file_song_id, " + 
                "su.upload_date as uploadDate, u.user_name, "+
                "group_concat(g.genre_name) genres, " + 
                "group_concat(distinct a.album_name) albumNames "+
                "from song s " + 
                "left join song_approval sa on s.song_id=sa.song_id " + 
                "left join song_upload su on s.song_id=su.song_id " + 
                "left join user u on su.uploaded_by=u.user_id " + 
                "left join song_genre sg on s.song_id=sg.song_id " + 
                "left join genre g on sg.genre_id=g.genre_id " + 
                "left join album_song als on als.song_id = s.song_id "+
                "left join album a on a.album_id = als.album_id "+
                "where sa.approval_status='APPROVED' " +
                "AND s.song_id = :songId "+ 
                "group by  s.song_id, s.title,s.artist, " + 
                "s.song_image, s.file_song_id, " + 
                "su.upload_date , u.user_name "
                , nativeQuery = true)
        Object findSongWithApproved(@Param("songId") Long songId);

        @Override
        @Query("SELECT s.songId "+
                "FROM Song s "+
                "LEFT JOIN s.songUploads su "+
                "WHERE su.uploadId = :uploadId"
        )
        Long findByUploadId(@Param("uploadId") Long uploadId);


}
