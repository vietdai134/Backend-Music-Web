package com.app.Music_Web.Infrastructure.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Entities.UserStatus;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserStatusRepository;

import java.io.IOException;
import java.util.Date;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String email = null;
        String jwt = null;

        // Đọc token từ cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    System.out.println("JWT from cookie: " + jwt); // Debug
                    break;
                }
            }
        }

        // Nếu không có trong cookie, thử đọc từ header Authorization (tùy chọn)
        if (jwt == null) {
            final String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                System.out.println("JWT from header: " + jwt); // Debug
            }
        }

        if (jwt != null) {
            try {
                email = jwtUtil.extractEmail(jwt);
            } catch (Exception e) {
                System.out.println("Invalid JWT token: " + e.getMessage());
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (jwtUtil.validateToken(jwt, email)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set for user: " + email + " with authorities: " + userDetails.getAuthorities());

                // Cập nhật trạng thái đăng nhập
                updateUserStatus(email);
            } else {
                System.out.println("JWT validation failed for token: " + jwt);
            }
        }

        chain.doFilter(request, response);
    }
    
    private void updateUserStatus(String email) {
        User user = userRepository.findByEmail(new UserEmail(email));
        if (user != null) {
            boolean hasActiveStatus = userStatusRepository.existsByUserAndIsActive(user, true);

            if (!hasActiveStatus) {
                // Nếu chưa có bản ghi active, tạo mới
                UserStatus userStatus = new UserStatus();
                userStatus.setUser(user);
                userStatus.setActive(true);
                userStatus.setLastLogin(new Date());
    
                userStatusRepository.save(userStatus);
                System.out.println("Created new UserStatus for userId: " + user.getUserId() + " with isActive: " + userStatus.isActive());
            } else {
                System.out.println("User already has an active session, skipping status creation.");
            }
        } else {
            System.out.println("User not found for email: " + email);
        }
    }
}