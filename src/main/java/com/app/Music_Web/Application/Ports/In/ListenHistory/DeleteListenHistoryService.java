package com.app.Music_Web.Application.Ports.In.ListenHistory;

public interface DeleteListenHistoryService {
    void deleteSongFromListenHistory(Long songId, String email);
}
