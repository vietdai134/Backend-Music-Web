package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.UserPayment;

public interface UserPaymentRepositoryPort {
    UserPayment save(UserPayment userPayment);
}
