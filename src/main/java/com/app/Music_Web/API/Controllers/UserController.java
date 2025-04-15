package com.app.Music_Web.API.Controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.ChangePasswordRequest;
import com.app.Music_Web.API.Request.RegisterRequest;
import com.app.Music_Web.API.Request.UserRequest;
import com.app.Music_Web.API.Response.UserResponse;
import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Application.Ports.In.User.DeleteUserService;
import com.app.Music_Web.Application.Ports.In.User.FindUserService;
import com.app.Music_Web.Application.Ports.In.User.RegisterService;
import com.app.Music_Web.Application.Ports.In.User.UpdateUserService;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Infrastructure.Persistence.CustomUserDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final RegisterService registerService;
    private final FindUserService findUserService;
    private final DeleteUserService deleteUserService;
    private final UpdateUserService updateUserService;
    private PasswordEncoder passwordEncoder;
    public UserController(
                    RegisterService registerService, 
                    FindUserService findUserService, 
                    DeleteUserService deleteUserService,
                    UpdateUserService updateUserService,
                    PasswordEncoder passwordEncoder) {
        this.registerService=registerService;
        this.findUserService=findUserService;
        this.deleteUserService=deleteUserService;
        this.updateUserService=updateUserService;
        this.passwordEncoder=passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> userRegister(@RequestBody RegisterRequest request) {
        registerService.userRegister(request.getUserName(), 
                            request.getEmail(), request.getPassword());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/create",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> userCreate(@ModelAttribute UserRequest request) throws Exception{
        System.out.println("Received: userName=" + request.getUserName() + 
                      ", avatar=" + (request.getAvatar() != null ? request.getAvatar().getOriginalFilename() : "null") +
                      ", roleNames=" + request.getRoleNames());
        registerService.userCreate(request.getUserName(), 
                            request.getEmail(), 
                            request.getPassword(),
                            request.getAccountType(),
                            request.getRoleNames(),
                            request.getAvatar());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        String email= customUserDetails.getEmail();
        
        // Tìm user theo email
        UserDTO user = findUserService.findUserEmail(new UserEmail(email));

        // Lấy danh sách permissions từ CustomUserDetails
        List<String> permissions = customUserDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority) // Lấy tên permission từ GrantedAuthority
            .collect(Collectors.toList());

        UserResponse userResponse = UserResponse.builder()
            .userId(user.getUserId())
            .userName(user.getUserName())
            .email(user.getEmail()) // Trả về cả email
            .accountType(user.getAccountType().toString())
            .permissions(permissions)
            .avatar(user.getUserAvatar())
            .build();

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    public ResponseEntity<Page<UserResponse>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.unsorted());
        Page<UserDTO> users = findUserService.findAllWithRoles(pageable);
        Page<UserResponse> userResponses =users.map(user-> UserResponse.builder()
                                            .userId(user.getUserId())
                                            .userName(user.getUserName())
                                            .accountType(user.getAccountType().toString())
                                            .createdDate(user.getCreatedDate())
                                            .roles(user.getRoles())
                                            .email(user.getEmail())
                                            .avatar(user.getUserAvatar())
                                            .build());
        return ResponseEntity.ok(userResponses);
    }

    @DeleteMapping("delete/{userId}")
    @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) throws Exception{
        deleteUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    //tìm kiếm gần đúng với phân trang
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    public ResponseEntity<Page<UserResponse>> searchUsersFuzzy(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        Page<UserDTO> users = findUserService.searchByUserNameOrEmail(keyword, pageable);
        Page<UserResponse> userResponses = users.map(user -> UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .accountType(user.getAccountType().toString())
                .createdDate(user.getCreatedDate())
                .roles(user.getRoles())
                .email(user.getEmail())
                .avatar(user.getUserAvatar())
                .build());
        return ResponseEntity.ok(userResponses);
    }


    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId){
        UserDTO user= findUserService.findUserById(userId);
        UserResponse userResponse= UserResponse.builder()
                                    .userId(userId)
                                    .userName(user.getUserName())
                                    .email(user.getEmail())
                                    .accountType(user.getAccountType().toString())
                                    .roles(user.getRoles())
                                    .avatar(user.getUserAvatar())
                                    .build();
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping(value = "/update/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    public ResponseEntity<Void> updateUser(
            @PathVariable Long userId,
            // @RequestBody UserUpdateRequest request) throws Exception{
            @ModelAttribute UserRequest.UserUpdateRequest request) throws Exception {
        System.out.println("Updating userId=" + userId + ", avatar=" + 
        (request.getAvatar() != null ? request.getAvatar().getOriginalFilename() : "null"));
        updateUserService.updateUser(
                userId,
                request.getUserName(),
                request.getEmail(),
                request.getAccountType(),
                request.getRoleNames(),
                request.getAvatar());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/changePassword")
    public ResponseEntity<Map<String, String>> getPassword(
        @RequestBody ChangePasswordRequest request,
        @AuthenticationPrincipal UserDetails userDetails
        ){
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;
        boolean isValid = passwordEncoder.matches(request.getOldPassword(), customUserDetails.getPassword());
        if (isValid) { 
            updateUserService.changePassword(customUserDetails.getUserId(), request.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Mật khẩu đúng, mật khẩu đã được cập nhật"));
        } else {
            // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Mật khẩu không đúng"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Mật khẩu hiện tại không đúng!"));
        }
    }

    @PutMapping("/image")
    public ResponseEntity<Void> userUpdateImg(
        @ModelAttribute UserRequest.UserUpdateRequest request,
        @AuthenticationPrincipal UserDetails userDetails
    ) throws Exception {
        CustomUserDetails customUserDetails= (CustomUserDetails) userDetails;
        Long userId = customUserDetails.getUserId();
        updateUserService.userUpdateImage(userId, request.getAvatar());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/info")
    public ResponseEntity<Void> userUpdateInfo(
        @RequestParam String userName,
        @AuthenticationPrincipal UserDetails userDetails
    ) throws Exception{
        CustomUserDetails customUserDetails= (CustomUserDetails) userDetails;
        Long userId = customUserDetails.getUserId();
        updateUserService.userUpdateInfo(userId, userName);
        return ResponseEntity.ok().build();
    }
}
