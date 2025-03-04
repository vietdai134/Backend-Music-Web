package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.ListenHistoryRepositoryPort;
import com.app.Music_Web.Domain.Entities.ListenHistory;

public interface ListenHistoryRepository extends JpaRepository<ListenHistory,Long>,ListenHistoryRepositoryPort{
    
}
