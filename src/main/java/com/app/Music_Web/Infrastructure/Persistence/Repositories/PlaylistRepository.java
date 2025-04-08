package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.app.Music_Web.Application.Ports.Out.PlaylistRepositoryPort;
import com.app.Music_Web.Domain.Entities.Playlist;

import io.lettuce.core.dynamic.annotation.Param;

public interface PlaylistRepository extends JpaRepository<Playlist,Long>,PlaylistRepositoryPort{
    @Override
    @Modifying
    @Query("UPDATE Playlist p "+
            "SET p.playlistName = :playlistName "+
            "WHERE p.playlistId = :playlistId"
    )
    void updatePlaylistName(@Param("playlistId") Long playlistId, 
                            @Param("playlistName") String playlistName);
}
