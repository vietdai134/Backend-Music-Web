package com.app.Music_Web.Application.Ports.Out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Domain.Entities.ListenHistory;

public interface ListenHistoryRepositoryPort {
    ListenHistory save(ListenHistory listenHistory);
    ListenHistory findBySong_SongIdAndUser_UserId(Long songId, Long userId);
    Page<Object[]> findByUser_UserId(Long userId, Pageable pageable);
    void deleteBySong_SongIdAndUser_UserId(Long songId, Long userId);
}
