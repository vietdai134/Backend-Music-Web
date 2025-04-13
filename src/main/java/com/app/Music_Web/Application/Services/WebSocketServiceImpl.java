package com.app.Music_Web.Application.Services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.app.Music_Web.Application.Ports.In.WebSocket.SendStatusService;

@Service
public class WebSocketServiceImpl implements SendStatusService{

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void notifyStatusChange(Long songId, String newStatus) {
        Map<String, Object> statusUpdate = new HashMap<>();
        statusUpdate.put("songId", songId);
        statusUpdate.put("status", newStatus);
        System.out.println("Sending WebSocket message: " + statusUpdate);
        // Gửi qua WebSocket
        messagingTemplate.convertAndSend("/topic/song-status", statusUpdate);
    }
    
}
