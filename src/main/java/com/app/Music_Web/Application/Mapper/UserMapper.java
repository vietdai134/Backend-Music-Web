package com.app.Music_Web.Application.Mapper;

import java.util.stream.Collectors;

import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Domain.Entities.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
            .userId(user.getUserId())
            .userName(user.getUserName().getUserName())
            .email(user.getEmail().getEmail())
            .accountType(user.getAccountType())
            .createdDate(user.getCreatedDate())
            // .password(user.getPassword().getPassword())
            .roles(user.getUserRoles().stream()
                       .map(userRole -> RoleMapper.toDTO(userRole.getRole())) // Lấy Role từ UserRole
                       .collect(Collectors.toList()))
            .build();
    }
}
