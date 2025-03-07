package com.app.Music_Web.Application.Ports.In.Auth;

import com.app.Music_Web.Application.DTO.UserAuthDTO;

public interface AuthService {
    UserAuthDTO login (String email, String password);
}
