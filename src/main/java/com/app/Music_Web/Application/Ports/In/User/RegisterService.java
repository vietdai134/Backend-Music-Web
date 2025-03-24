package com.app.Music_Web.Application.Ports.In.User;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.Music_Web.Application.DTO.UserDTO;

public interface RegisterService {
    UserDTO userRegister(String userName, String email, String password);
    UserDTO userCreate(String userName, String email, String password,
                            String accountType,List<String> roleNames,
                            MultipartFile avatar)throws Exception;
}
