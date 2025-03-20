package com.app.Music_Web.Application.Services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Application.Mapper.UserMapper;
import com.app.Music_Web.Application.Ports.In.User.DeleteUserService;
import com.app.Music_Web.Application.Ports.In.User.FindUserService;
import com.app.Music_Web.Application.Ports.In.User.RegisterService;
import com.app.Music_Web.Application.Ports.In.User.UpdateUserService;
import com.app.Music_Web.Application.Ports.Out.RoleRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.Role;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Entities.UserRole;
import com.app.Music_Web.Domain.Enums.AccountType;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Domain.ValueObjects.User.UserName;
import com.app.Music_Web.Domain.ValueObjects.User.UserPassword;

@Service
public class UserServiceImpl implements RegisterService, FindUserService,DeleteUserService, UpdateUserService {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepositoryPort roleRepositoryPort;
    public UserServiceImpl (UserRepositoryPort userRepositoryPort, RoleRepositoryPort roleRepositoryPort){
        this.userRepositoryPort=userRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
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
                        .userRoles(new ArrayList<>())
                        .build();
                        
        Role defaultRole = roleRepositoryPort.findByRoleName("USER");
        UserRole userRole= UserRole.builder()
                        .user(user)
                        .role(defaultRole)
                        .grantedDate(new Date())
                        .build();
                        user.getUserRoles().add(userRole);

        User userRegister= userRepositoryPort.save(user);
        return UserMapper.toDTO(userRegister);
    }

    @Override
    public UserDTO userCreate(String userName, String email, String password,String accountType,List<String> roleNames) {
        String hashedPassword= passwordEncoder.encode(password);
        User user = User.builder()
                        .userName(new UserName(userName))
                        .email(new UserEmail(email))
                        .password(new UserPassword(hashedPassword))
                        // .accountType(AccountType.NORMAL)
                        .accountType(AccountType.valueOf(accountType.toUpperCase()))
                        .createdDate(new Date())
                        .userRoles(new ArrayList<>())
                        .build();
        
        // Lấy danh sách các vai trò từ repository dựa trên danh sách tên vai trò
        List<Role> roles = roleRepositoryPort.findByRoleNameIn(roleNames);
         // Tạo danh sách UserRole
        List<UserRole> userRoles = roles.stream()
            .map(role -> UserRole.builder()
                .user(user)
                .role(role)
                .grantedDate(new Date())
                .build())
            .collect(Collectors.toList());

        user.getUserRoles().addAll(userRoles); 

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

    @Override
    public UserDTO findUserById(Long userId) {
        User user = userRepositoryPort.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        return UserMapper.toDTO(user);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepositoryPort.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        userRepositoryPort.delete(user);
    }

    @Override
    public Page<UserDTO> findAllWithRoles(Pageable pageable) {
        Page<User> users = userRepositoryPort.findAllWithRoles(pageable);
        return users.map(UserMapper::toDTO);
    }

    // Thêm phương thức tìm kiếm gần đúng với phân trang
    @Override
    public Page<UserDTO> searchByUserNameOrEmail(String keyword, Pageable pageable) {
        Page<User> users = userRepositoryPort.searchByUserNameOrEmail(keyword, pageable);
        return users.map(UserMapper::toDTO);
    }
    
    @Override
    public UserDTO updateUser(Long userId, String userName, String email, String accountType, List<String> roleNames) {
        // Tìm user theo userId
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + userId));

        // Cập nhật các thuộc tính
        user.setUserName(new UserName(userName));
        user.setEmail(new UserEmail(email));
        user.setAccountType(AccountType.valueOf(accountType.toUpperCase()));

        // Xóa các UserRole cũ
        user.getUserRoles().clear();

        // Lấy danh sách các vai trò mới từ repository dựa trên roleNames
        List<Role> roles = roleRepositoryPort.findByRoleNameIn(roleNames);
        
        // Tạo danh sách UserRole mới
        List<UserRole> userRoles = roles.stream()
            .map(role -> UserRole.builder()
                .user(user)
                .role(role)
                .grantedDate(new Date()) // Giữ ngày cấp mới hoặc dùng lại ngày cũ nếu cần
                .build())
            .collect(Collectors.toList());

        // Thêm UserRole mới vào user
        user.getUserRoles().addAll(userRoles);

        // Lưu user đã cập nhật
        User updatedUser = userRepositoryPort.save(user);
        return UserMapper.toDTO(updatedUser);
    }

}
