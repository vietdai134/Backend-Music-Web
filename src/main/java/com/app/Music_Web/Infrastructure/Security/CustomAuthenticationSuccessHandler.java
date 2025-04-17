package com.app.Music_Web.Infrastructure.Security;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.app.Music_Web.Application.DTO.UserAuthDTO;
import com.app.Music_Web.Application.Ports.In.Auth.AuthService;

import java.io.IOException;
import java.time.Duration;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService authService; // Thay bằng service xử lý đăng nhập Google của bạn

    public CustomAuthenticationSuccessHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        UserAuthDTO authDTO = authService.loginWithGoogle(oidcUser);

        // Thiết lập cookie accessToken
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", authDTO.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite("Strict")
                .build();

        // Thiết lập cookie refreshToken
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authDTO.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Strict")
                .build();

        // Thêm cookie vào response
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // Chuyển hướng đến route frontend
        String redirectUrl = UriComponentsBuilder.fromUriString("https://localhost:4200/")
                .build()
                .toUriString();
        response.sendRedirect(redirectUrl);
    }
}
