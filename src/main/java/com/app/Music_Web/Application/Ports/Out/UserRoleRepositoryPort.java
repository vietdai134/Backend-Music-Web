package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Entities.UserRole;

public interface UserRoleRepositoryPort {
    UserRole save(UserRole userRole);
    List<UserRole> findByUser(User user);
}
