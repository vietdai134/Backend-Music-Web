package com.app.Music_Web.Application.Ports.In.ListenHistory;

public interface SaveListenHistoryService {
    void saveListenHistory(Long songId, String email);  // Save the listen history for a specific song and user
}
