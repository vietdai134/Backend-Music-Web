package com.app.Music_Web.API.Controllers;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.ForgotPasswordRequest;
import com.app.Music_Web.API.Request.LoginRequest;
import com.app.Music_Web.API.Request.ResetPasswordRequest;
import com.app.Music_Web.Application.DTO.UserAuthDTO;
import com.app.Music_Web.Application.Ports.In.Auth.AuthService;
import com.app.Music_Web.Application.Ports.In.Auth.ForgotPasswordService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;
    public AuthController(
        AuthService authService,
        ForgotPasswordService forgotPasswordService){
        this.authService=authService;
        this.forgotPasswordService=forgotPasswordService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        UserAuthDTO authDTO = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

        ResponseCookie  accessCookie = ResponseCookie.from("accessToken", authDTO.getAccessToken())
                                        .httpOnly(true)
                                        .secure(true)
                                        .path("/")
                                        // .maxAge(Duration.ofHours(1)) // 1 giờ
                                        .maxAge(Duration.ofMinutes(15))
                                        .sameSite("Strict") // Chống CSRF
                                        .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authDTO.getRefreshToken())
                                        .httpOnly(true)
                                        .secure(true)
                                        .sameSite("Strict")
                                        .maxAge(Duration.ofDays(1))
                                        // .maxAge(Duration.ofMinutes(1))
                                        .path("/")
                                        .build();

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Login successful");

        return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                            .body(responseBody);
                            // .build();
    }

        @PostMapping("/logout")
        public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Logout successful");
        return ResponseEntity.ok(responseBody);
        }

        @PostMapping("/refresh")
        public ResponseEntity<Map<String, String>> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        UserAuthDTO authDTO = authService.refreshAccessToken(refreshToken);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", authDTO.getAccessToken())
                                                .httpOnly(true)
                                                .secure(true)
                                                .path("/")
                                                .maxAge(Duration.ofMinutes(15))
                                                .sameSite("Strict")
                                                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authDTO.getRefreshToken())
                                                .httpOnly(true)
                                                .secure(true)
                                                .path("/")
                                                .maxAge(Duration.ofDays(1))
                                                .sameSite("Strict")
                                                .build();

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", "Token refreshed");

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(responseBody);
        }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        forgotPasswordService.sendResetLink(request.getEmail());
        return ResponseEntity.ok(Map.of("message", "Đã gửi link reset về email nếu tồn tại"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        forgotPasswordService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Đặt lại mật khẩu thành công"));
    }

}
