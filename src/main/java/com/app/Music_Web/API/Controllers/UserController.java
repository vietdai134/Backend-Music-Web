package com.app.Music_Web.API.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.UserRequest;
import com.app.Music_Web.API.Response.UserResponse;
import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Application.Ports.In.User.DeleteUserService;
import com.app.Music_Web.Application.Ports.In.User.FindUserService;
import com.app.Music_Web.Application.Ports.In.User.RegisterService;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Infrastructure.Persistence.CustomUserDetails;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final RegisterService registerService;
    private final FindUserService findUserService;
    private final DeleteUserService deleteUserService;

    public UserController(
                    RegisterService registerService, 
                    FindUserService findUserService, 
                    DeleteUserService deleteUserService) {
        this.registerService=registerService;
        this.findUserService=findUserService;
        this.deleteUserService=deleteUserService;
    }

    @PostMapping
    public ResponseEntity<Void> userRegister(@RequestBody UserRequest request) {
        registerService.userRegister(request.getUserName(), 
                            request.getEmail(), request.getPassword());
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
            .build();

        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/all")
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
                                            .build());
        return ResponseEntity.ok(userResponses);
    }

    @DeleteMapping("delete/{userId}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long userId){
        deleteUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Thêm endpoint tìm kiếm gần đúng với phân trang
    @GetMapping("/search")
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
                .build());
        return ResponseEntity.ok(userResponses);
    }

}
