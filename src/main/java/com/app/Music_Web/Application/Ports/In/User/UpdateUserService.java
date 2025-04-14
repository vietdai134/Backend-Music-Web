package com.app.Music_Web.Application.Ports.In.User;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.Music_Web.Application.DTO.UserDTO;

public interface UpdateUserService {
    UserDTO updateUser(Long userId, String userName, String email, 
                        String accountType, List<String> roleNames,
                        MultipartFile avatar)throws Exception;

    void changePassword(Long userId, String newPassword);
}
