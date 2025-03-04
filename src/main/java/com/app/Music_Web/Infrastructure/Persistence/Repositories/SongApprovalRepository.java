package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.SongApprovalRepositoryPort;
import com.app.Music_Web.Domain.Entities.SongApproval;

public interface SongApprovalRepository extends JpaRepository<SongApproval,Long>, SongApprovalRepositoryPort{
    
}
