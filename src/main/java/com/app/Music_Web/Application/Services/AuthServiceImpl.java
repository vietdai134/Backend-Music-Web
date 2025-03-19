package com.app.Music_Web.Application.Services;

import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.UserAuthDTO;
import com.app.Music_Web.Application.Ports.In.Auth.AuthService;
import com.app.Music_Web.Application.Mapper.UserAuthMapper;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Entities.UserAuth;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Infrastructure.Security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.UUID;
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl (UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder,
                            JwtUtil jwtUtil){
        this.userRepositoryPort=userRepositoryPort;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil=jwtUtil;
    }
    

    @Override
    public UserAuthDTO login(String email, String password) {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);
        if (user == null || !passwordEncoder.matches(password, user.getPassword().getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String accessToken = jwtUtil.generateToken(email);

        UserAuth userAuth = user.getUserAuths().stream()
                .findFirst()
                .orElse(null); // Lấy UserAuth đầu tiên hoặc null nếu không có
        // Thời gian sống của refresh token là 1 phút
        // long refreshTokenExpiryMs = 1000 * 60;
        long refreshTokenExpiryMs = 1000L * 60 * 60 * 24; // 1 ngày
        // Kiểm tra xem UserAuth có tồn tại và refreshToken có hết hạn không
        if (userAuth == null || userAuth.getRefreshTokenExpiry().before(new Date())) {
            // Nếu không có UserAuth hoặc refreshToken đã hết hạn, tạo mới
            userAuth = UserAuth.builder()
                    .user(user)
                    .refreshToken(UUID.randomUUID().toString())
                    // .refreshTokenExpiry(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1h
                    .refreshTokenExpiry(new Date(System.currentTimeMillis() + refreshTokenExpiryMs)) // 1 phút
                    .build();
            if (!user.getUserAuths().contains(userAuth)) {
                user.getUserAuths().clear(); // Xóa UserAuth cũ nếu có
                user.getUserAuths().add(userAuth); // Thêm UserAuth mới
            }
        }

        userRepositoryPort.save(user); // Lưu vào database

        return UserAuthMapper.toDTO(userAuth, accessToken);
    }

    @Override
    public UserAuthDTO refreshAccessToken(String refreshToken) {
        User user = userRepositoryPort.findByUserAuths_RefreshToken(refreshToken);
        if (user == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        UserAuth userAuth = user.getUserAuths().stream()
                .filter(auth -> auth.getRefreshToken().equals(refreshToken))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (userAuth.getRefreshTokenExpiry().before(new Date())) {
            throw new RuntimeException("Refresh token has expired");
        }

        String email = user.getEmail().getEmail();
        String newAccessToken = jwtUtil.generateToken(email);

        long refreshTokenExpiryMs = 1000L * 60 * 60 * 24; // 1 ngày
        userAuth.setRefreshTokenExpiry(new Date(System.currentTimeMillis() + refreshTokenExpiryMs));
        userRepositoryPort.save(user);

        return UserAuthMapper.toDTO(userAuth, newAccessToken); // Giữ nguyên refresh token cũ
    }
}
