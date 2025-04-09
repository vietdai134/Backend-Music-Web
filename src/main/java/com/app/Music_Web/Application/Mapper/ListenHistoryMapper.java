package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.ListenHistoryDTO;
import com.app.Music_Web.Domain.Entities.ListenHistory;

public class ListenHistoryMapper {
        public static ListenHistoryDTO toListenHistoryDTO(ListenHistory listenHistory) {
        return ListenHistoryDTO.builder()
                .historyId(listenHistory.getHistoryId())
                .listenedDate(listenHistory.getListenedDate())
                .songId(listenHistory.getSong().getSongId())
                .userId(listenHistory.getUser().getUserId())
                .build();
    }
}
