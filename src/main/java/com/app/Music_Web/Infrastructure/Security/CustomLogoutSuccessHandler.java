package com.app.Music_Web.Infrastructure.Security;

import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Entities.UserStatus;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserStatusRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final JwtUtil jwtUtil;

    public CustomLogoutSuccessHandler(UserRepository userRepository, UserStatusRepository userStatusRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String jwt = null;

        // Đọc token từ cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        if (jwt != null) {
            String email = jwtUtil.extractEmail(jwt);
            User user = userRepository.findByEmail(new UserEmail(email));
            if (user != null) {
                // Tìm bản ghi gần nhất của user theo thời gian LastLogin
                UserStatus userStatus = userStatusRepository.findTopByUserOrderByLastLoginDesc(user).orElse(null);
                if (userStatus != null) {
                    userStatus.setActive(false);
                    userStatus.setLastLogin(new Date());
                    userStatusRepository.save(userStatus);
                    System.out.println("Updated latest UserStatus for userId: " + user.getUserId() + " to isActive: " + userStatus.isActive());
                }
            }
        }

        ResponseCookie deleteAccessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        ResponseCookie deleteRefreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteAccessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, deleteRefreshCookie.toString());

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\": \"Logout successful\"}");
    }
}
