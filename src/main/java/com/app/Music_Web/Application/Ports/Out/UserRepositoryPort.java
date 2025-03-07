package com.app.Music_Web.Application.Ports.Out;

import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;

public interface UserRepositoryPort {
    User save(User user);
    User findByEmail(UserEmail email);
} 
