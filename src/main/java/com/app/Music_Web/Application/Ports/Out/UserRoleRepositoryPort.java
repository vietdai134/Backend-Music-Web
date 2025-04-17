package com.app.Music_Web.Application.Ports.Out;

import java.util.List;

import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Entities.UserRole;

public interface UserRoleRepositoryPort {
    List<UserRole> findByUser(User user);
}
