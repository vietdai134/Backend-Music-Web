package com.app.Music_Web.Application.Mapper;

import java.util.stream.Collectors;

import com.app.Music_Web.Application.DTO.RoleDTO;
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
            .userAvatar(user.getUserAvatar())
            .roles(user.getUserRoles().stream()
                    //    .map(userRole -> RoleMapper.toDTO(userRole.getRole())) // Lấy Role từ UserRole
                        .map(userRole -> RoleDTO.builder()
                           .roleId(userRole.getRole().getRoleId())
                           .roleName(userRole.getRole().getRoleName())
                           .description(userRole.getRole().getDescription())
                        //    .permissions(userRole.getRole().getPermissions())
                           .grantedDate(userRole.getGrantedDate()) // Gán giá trị grantedDate
                           .build())
                       .collect(Collectors.toList()))
            .build();
    }
}
