package com.app.Music_Web.Application.Ports.Out;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;

public interface UserRepositoryPort {
    Page<User> findAllWithRoles(Pageable pageable);
    Page<User> searchByUserNameOrEmail(String keyword, Pageable pageable);

    User save(User user);

    void delete(User user);

    User findByEmail(UserEmail email);
    Optional<User> findById(Long userId);
    User findByUserAuths_RefreshToken(String refreshToken);
} 
