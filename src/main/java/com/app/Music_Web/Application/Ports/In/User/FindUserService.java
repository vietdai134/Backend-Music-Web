package com.app.Music_Web.Application.Ports.In.User;

import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;

public interface FindUserService {
    UserDTO findUserEmail (UserEmail userEmail);
    UserDTO findUserById (Long userId);
}
