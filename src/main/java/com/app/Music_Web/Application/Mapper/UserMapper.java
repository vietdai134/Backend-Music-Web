package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Domain.Entities.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
            .userId(user.getUserId())
            .userName(user.getUserName().getUserName())
            .email(user.getEmail().getEmail())
            .password(user.getPassword().getPassword())
            .build();
    }
}
