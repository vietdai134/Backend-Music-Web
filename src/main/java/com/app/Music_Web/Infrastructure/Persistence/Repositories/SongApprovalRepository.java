package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.Music_Web.Application.Ports.Out.SongApprovalRepositoryPort;
import com.app.Music_Web.Domain.Entities.SongApproval;
import com.app.Music_Web.Domain.Enums.ApprovalStatus;

public interface SongApprovalRepository extends JpaRepository<SongApproval,Long>, SongApprovalRepositoryPort{
    @Override
    @Modifying
    @Query("UPDATE SongApproval sa "+
            "SET sa.approvalStatus = :approvalStatus "+
            "WHERE sa.song.songId = :songId"
    )
    void updateStatusSong(@Param("songId") Long songId, 
                        @Param("approvalStatus") ApprovalStatus approvalStatus);
}
