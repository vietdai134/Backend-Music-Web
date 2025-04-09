package com.app.Music_Web.Application.Ports.In.ListenHistory;

import java.util.List;

import com.app.Music_Web.Application.DTO.ListenHistoryDTO;

public interface FindListenHistoryService {
    List<ListenHistoryDTO> findListenHistoryByUser(String email); 
}
