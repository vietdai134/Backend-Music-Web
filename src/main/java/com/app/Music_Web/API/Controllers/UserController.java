package com.app.Music_Web.API.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.UserRequest;
import com.app.Music_Web.API.Response.UserResponse;
import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Application.Ports.In.User.FindUserService;
import com.app.Music_Web.Application.Ports.In.User.RegisterService;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Infrastructure.Persistence.CustomUserDetails;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final RegisterService registerService;
    private final FindUserService findUserService;

    public UserController(RegisterService registerService, FindUserService findUserService){
        this.registerService=registerService;
        this.findUserService=findUserService;
    }

    @PostMapping
    public UserResponse userRegister(@RequestBody UserRequest request) {
        UserDTO savedUser = registerService.userRegister(request.getUserName(), 
                            request.getEmail(), request.getPassword());
        return UserResponse.builder()
                .userId(savedUser.getUserId())
                .userName(savedUser.getUserName())
                .build();
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

}
