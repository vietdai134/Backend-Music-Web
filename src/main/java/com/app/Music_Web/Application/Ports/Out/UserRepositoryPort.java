package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.User;

public interface UserRepositoryPort {
    User save(User user);
    
} 
