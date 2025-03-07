package com.app.Music_Web.API.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.LoginRequest;
import com.app.Music_Web.API.Response.AuthResponse;
import com.app.Music_Web.Application.DTO.UserAuthDTO;
import com.app.Music_Web.Application.Ports.In.Auth.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService=authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        UserAuthDTO userAuthDTO = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        AuthResponse response = AuthResponse.builder()
                .accessToken(userAuthDTO.getAccessToken())
                .refreshToken(userAuthDTO.getRefreshToken())
                .build();
        return ResponseEntity.ok(response);
    }
}
