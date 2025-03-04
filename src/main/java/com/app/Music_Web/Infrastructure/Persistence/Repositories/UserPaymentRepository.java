package com.app.Music_Web.Infrastructure.Persistence.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.Music_Web.Application.Ports.Out.UserPaymentRepositoryPort;
import com.app.Music_Web.Domain.Entities.UserPayment;

public interface UserPaymentRepository extends JpaRepository<UserPayment,Long>,UserPaymentRepositoryPort{
    
}
