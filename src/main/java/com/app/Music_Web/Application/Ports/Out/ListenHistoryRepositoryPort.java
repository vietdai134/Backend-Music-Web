package com.app.Music_Web.Application.Ports.Out;


import java.util.List;

import com.app.Music_Web.Domain.Entities.ListenHistory;

public interface ListenHistoryRepositoryPort {
    ListenHistory save(ListenHistory listenHistory);
    ListenHistory findBySong_SongIdAndUser_UserId(Long songId, Long userId);
    void deleteBySong_SongIdAndUser_UserId(Long songId, Long userId);

    List<ListenHistory> findAllByUser_UserId(Long userId);
}
