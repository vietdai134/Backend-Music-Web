package com.app.Music_Web.Application.Ports.Out;

import java.util.Optional;

import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;

public interface UserRepositoryPort {
    User save(User user);
    User findByEmail(UserEmail email);
    Optional<User> findById(Long userId);
    User findByUserAuths_RefreshToken(String refreshToken);
} 
