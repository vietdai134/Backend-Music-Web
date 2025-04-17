package com.app.Music_Web.API.Controllers;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.ForgotPasswordRequest;
import com.app.Music_Web.API.Request.LoginRequest;
import com.app.Music_Web.API.Request.ResetPasswordRequest;
import com.app.Music_Web.Application.DTO.RegisterTempData;
import com.app.Music_Web.Application.DTO.UserAuthDTO;
import com.app.Music_Web.Application.Ports.In.Auth.AuthService;
import com.app.Music_Web.Application.Ports.In.Auth.CheckVerificationService;
import com.app.Music_Web.Application.Ports.In.Auth.ForgotPasswordService;
import com.app.Music_Web.Application.Ports.In.User.RegisterService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import redis.clients.jedis.JedisPooled;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final ForgotPasswordService forgotPasswordService;
    private final CheckVerificationService checkVerificationService;
    private final JedisPooled jedis;
    private final RegisterService registerService;
    public AuthController(
        AuthService authService,
        ForgotPasswordService forgotPasswordService,
        JedisPooled jedis,
        RegisterService registerService,
        CheckVerificationService checkVerificationService){
        this.authService=authService;
        this.forgotPasswordService=forgotPasswordService;
        this.jedis=jedis;
        this.registerService = registerService;
        this.checkVerificationService=checkVerificationService;
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

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token, HttpServletRequest request) {
        System.out.println("Verifying email with token: " + token + ", from IP: " + request.getRemoteAddr());
        String redisKey = "register:" + token;
        String userDataJson = jedis.get(redisKey);

        if (userDataJson == null) {
            return ResponseEntity.badRequest().body("Token không hợp lệ hoặc đã hết hạn.");
        }

        // Lưu thông tin người dùng vào database
        registerService.completeRegistration(userDataJson);

        // Xóa dữ liệu tạm thời khỏi Redis
        jedis.del(redisKey);
        try {
            RegisterTempData tempData = new ObjectMapper().readValue(userDataJson, RegisterTempData.class);
            jedis.del("email:" + tempData.getEmail());
        } catch (Exception e) {
            System.out.println("Error deleting email key: " + e.getMessage());
        }

        return ResponseEntity.ok("Xác minh email thành công. Bạn có thể đăng nhập.");
    }

    @GetMapping("/check-verification")
    public ResponseEntity<Map<String, Boolean>> checkVerification(@RequestParam("email") String email) {
        boolean isVerified = this.checkVerificationService.isEmailVerify(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isVerified", isVerified);
        return ResponseEntity.ok(response);
    }

}
