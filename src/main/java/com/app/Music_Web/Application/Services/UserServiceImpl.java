package com.app.Music_Web.Application.Services;

import java.util.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Application.Mapper.UserMapper;
import com.app.Music_Web.Application.Ports.In.User.FindUserService;
import com.app.Music_Web.Application.Ports.In.User.RegisterService;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Enums.AccountType;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Domain.ValueObjects.User.UserName;
import com.app.Music_Web.Domain.ValueObjects.User.UserPassword;

@Service
public class UserServiceImpl implements RegisterService, FindUserService {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl (UserRepositoryPort userRepositoryPort){
        this.userRepositoryPort=userRepositoryPort;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDTO userRegister(String userName, String email, String password) {
        String hashedPassword= passwordEncoder.encode(password);
        User user = User.builder()
                        .userName(new UserName(userName))
                        .email(new UserEmail(email))
                        .password(new UserPassword(hashedPassword))
                        .accountType(AccountType.NORMAL)
                        .createdDate(new Date())
                        .build();
        User userRegister= userRepositoryPort.save(user);
        return UserMapper.toDTO(userRegister);
    }

    @Override
    public UserDTO findUserEmail(UserEmail userEmail) {
        User user= userRepositoryPort.findByEmail(userEmail);
        if (user==null){
            throw new RuntimeException("Không tìm thấy user");
        }
        return UserMapper.toDTO(user);
    }
    
}
