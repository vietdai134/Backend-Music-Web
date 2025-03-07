package com.app.Music_Web.API.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.UserRequest;
import com.app.Music_Web.API.Response.UserResponse;
import com.app.Music_Web.Application.DTO.UserDTO;
import com.app.Music_Web.Application.Ports.In.User.FindUserService;
import com.app.Music_Web.Application.Ports.In.User.RegisterService;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;

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

    @GetMapping("find")
    public ResponseEntity<?> getUserEmail (@RequestParam String email){
        UserEmail userEmail= new UserEmail(email);
        UserDTO user = findUserService.findUserEmail(userEmail);
        UserResponse userResponse = UserResponse.builder()
                                    .userId(user.getUserId())
                                    .userName(user.getUserName())
                                    .build();
        return ResponseEntity.ok(userResponse);
    }
}
