package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.ListenHistory;

public interface ListenHistoryRepositoryPort {
    ListenHistory save(ListenHistory listenHistory);
}
