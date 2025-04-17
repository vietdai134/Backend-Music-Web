package com.app.Music_Web.Application.Ports.Out;

import java.util.Optional;

import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Entities.UserStatus;

public interface UserStatusRepositoryPort {
    Optional<UserStatus> findByUser(User user);
    Optional<UserStatus> findTopByUserOrderByLastLoginDesc(User user);
    boolean existsByUserAndIsActive(User user, boolean isActive);
}
