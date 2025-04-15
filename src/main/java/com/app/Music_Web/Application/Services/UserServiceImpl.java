package com.app.Music_Web.Application.Services;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Application.Mapper.UserMapper;
import com.app.Music_Web.Application.Ports.In.Cloudinary.CloudinaryService;
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
public class UserServiceImpl implements RegisterService, FindUserService,
                                    DeleteUserService, UpdateUserService {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepositoryPort roleRepositoryPort;
    private final CloudinaryService cloudinaryService;
    public UserServiceImpl (UserRepositoryPort userRepositoryPort, 
                            RoleRepositoryPort roleRepositoryPort,
                            CloudinaryService cloudinaryService){
        this.userRepositoryPort=userRepositoryPort;
        this.roleRepositoryPort = roleRepositoryPort;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.cloudinaryService= cloudinaryService;
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
                        .userAvatar("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743621035/NoIMG_tmvbrh.jpg")
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
    public UserDTO userCreate(String userName, String email, String password,
                                String accountType,List<String> roleNames,
                                MultipartFile userAvatar) throws Exception {
        System.out.println("Creating user: userName=" + userName + 
                            ", roleNames=" + roleNames);
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

        // Tạo CompletableFuture cho việc upload ảnh
        CompletableFuture<String> avatarFuture = (userAvatar != null && !userAvatar.isEmpty())
        ? CompletableFuture.supplyAsync(() -> {
            try {
                String folder = "users/userAvatars/" + userName;
                return cloudinaryService.uploadAvatar(userAvatar, folder);
            } catch (IOException e) {
                throw new RuntimeException("Upload failed", e);
            }
        })
        : CompletableFuture.completedFuture("https://res.cloudinary.com/dutcbjnyb/image/upload/v1743621035/NoIMG_tmvbrh.jpg");

        // Lấy danh sách các vai trò từ repository dựa trên danh sách tên vai trò
        List<Role> roles = roleRepositoryPort.findByRoleNameIn(roleNames);
        System.out.println("Found roles: " + roles);
         // Tạo danh sách UserRole  
        List<UserRole> userRoles = roles.stream()
            .map(role -> UserRole.builder()
                // .user(user)
                .user(user)
                .role(role)
                .grantedDate(new Date())
                .build())
            .collect(Collectors.toList());
        System.out.println("UserRoles created: " + userRoles.size());
        user.getUserRoles().addAll(userRoles);

        // Đợi URL ảnh và gán vào user
        String userAvatarUrl = avatarFuture.get(); // Chặn ở đây để lấy URL
        if (userAvatarUrl != null) {
            user.setUserAvatar(userAvatarUrl);
        }

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
    public void deleteUser(Long userId) throws Exception{
        User user = userRepositoryPort.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        if (user.getUserAvatar() != null && !user.getUserAvatar().isEmpty()) {
            String folder = "users/userAvatars/" + user.getUserName().getUserName();
            cloudinaryService.deleteFolder(folder);
        }
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
    public UserDTO updateUser(Long userId, String userName, String email, 
                                String accountType, List<String> roleNames,
                                MultipartFile userAvatar) throws Exception{
        // Tìm user theo userId
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + userId));

        // Cập nhật các thuộc tính
        user.setUserName(new UserName(userName));
        user.setEmail(new UserEmail(email));
        user.setAccountType(AccountType.valueOf(accountType.toUpperCase()));        

        CompletableFuture<String> avatarFuture = (userAvatar != null && !userAvatar.isEmpty())
        ? CompletableFuture.supplyAsync(() -> {
            try {
                // Xóa avatar cũ nếu có
                if (user.getUserAvatar() != null && !user.getUserAvatar().isEmpty()) {
                    String oldPublicId = cloudinaryService.extractPublicIdFromUrl(user.getUserAvatar());
                    if (oldPublicId != null) {
                        cloudinaryService.deleteAvatar(oldPublicId);
                    }
                }
                String folder = "users/userAvatars/" + userName;
                return cloudinaryService.uploadAvatar(userAvatar, folder);
            } catch (IOException e) {
                throw new RuntimeException("Upload failed", e);
            }
        })
        : CompletableFuture.completedFuture(user.getUserAvatar());
        // : CompletableFuture.completedFuture("https://res.cloudinary.com/dutcbjnyb/image/upload/v1742804273/users/userAvatars/default/sgsl4xyfmmsogrtozgmk.jpg");

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

        // Đợi URL ảnh và gán vào user
        String avatarUrl = avatarFuture.get(); // Chặn ở đây để lấy URL
        if (avatarUrl != null) {
            user.setUserAvatar(avatarUrl);
        }

        // Lưu user đã cập nhật
        User updatedUser=userRepositoryPort.save(user);
        return UserMapper.toDTO(updatedUser);
    }

    @Override
    public void changePassword(Long userId, String newPassword) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        user.setPassword(new UserPassword(passwordEncoder.encode(newPassword)));
        userRepositoryPort.save(user);
    }


    @Override
    public void userUpdateImage(Long userId, MultipartFile userAvatar) 
                        throws Exception{
        // Tìm user theo userId
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + userId));       

        CompletableFuture<String> avatarFuture = (userAvatar != null && !userAvatar.isEmpty())
        ? CompletableFuture.supplyAsync(() -> {
            try {
                // Xóa avatar cũ nếu có
                if (user.getUserAvatar() != null && !user.getUserAvatar().isEmpty()) {
                    String oldPublicId = cloudinaryService.extractPublicIdFromUrl(user.getUserAvatar());
                    if (oldPublicId != null) {
                        cloudinaryService.deleteAvatar(oldPublicId);
                    }
                }
                String folder = "users/userAvatars/" + user.getUserName().getUserName();
                return cloudinaryService.uploadAvatar(userAvatar, folder);
            } catch (IOException e) {
                throw new RuntimeException("Upload failed", e);
            }
        })
        : CompletableFuture.completedFuture(user.getUserAvatar());

        // Đợi URL ảnh và gán vào user
        String avatarUrl = avatarFuture.get(); // Chặn ở đây để lấy URL
        if (avatarUrl != null) {
            user.setUserAvatar(avatarUrl);
        }

        // Lưu user đã cập nhật
       userRepositoryPort.save(user);
    }


    @Override
    public void userUpdateInfo(Long userId, String userName) throws Exception{
        // Tìm user theo userId
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + userId));
        // Cập nhật các thuộc tính
        user.setUserName(new UserName(userName));
      
        // Lưu user đã cập nhật
        userRepositoryPort.save(user);
    }
}
