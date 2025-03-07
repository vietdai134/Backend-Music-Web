package com.app.Music_Web.Application.Ports.In.User;

import com.app.Music_Web.Application.DTO.UserDTO;

public interface RegisterService {
    UserDTO userRegister(String userName, String email, String password);
}
