package com.app.Music_Web.Application.Ports.In.WebSocket;

public interface SendStatusService {
    void notifyStatusChange(Long songId, String newStatus);
}
