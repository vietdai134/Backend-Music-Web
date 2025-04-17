package com.app.Music_Web.Application.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.Music_Web.Application.DTO.UserAuthDTO;
import com.app.Music_Web.Application.Ports.In.Auth.AuthService;
import com.app.Music_Web.Application.Ports.In.Auth.CheckVerificationService;
import com.app.Music_Web.Application.Ports.In.Auth.ForgotPasswordService;
import com.app.Music_Web.Application.Ports.In.Mail.MailService;
import com.app.Music_Web.Application.Ports.In.User.RegisterService;
import com.app.Music_Web.Application.Mapper.UserAuthMapper;
import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Entities.UserAuth;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Domain.ValueObjects.User.UserPassword;
import com.app.Music_Web.Infrastructure.Security.JwtUtil;

import redis.clients.jedis.JedisPooled;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
@Service
public class AuthServiceImpl implements AuthService, 
                                    ForgotPasswordService,
                                    CheckVerificationService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JedisPooled jedis;
    private final MailService mailService;
    private final RegisterService registerService;

    public AuthServiceImpl (UserRepositoryPort userRepositoryPort, 
                            PasswordEncoder passwordEncoder,
                            JwtUtil jwtUtil,
                            JedisPooled jedis,
                            MailService mailService,
                            RegisterService registerService){
        this.userRepositoryPort=userRepositoryPort;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtil=jwtUtil;
        this.jedis=jedis;
        this.mailService=mailService;
        this.registerService= registerService;
    }
    

    @Override
    public UserAuthDTO login(String email, String password) {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);
        if (!user.getAuthProvider().contains("LOCAL")) {
            throw new RuntimeException("User registered with Google only, please use Google login");
        }
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


    @Override
    public void sendResetLink(String email) {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);

        String token = UUID.randomUUID().toString();
        String redisKey = "reset:" + token;

        // Lưu userId vào Redis với thời hạn 15 phút
        jedis.setex(redisKey, 900, String.valueOf(user.getUserId()));

        // In ra giả lập gửi email
        String resetLink = "https://localhost:4200/reset-password?token=" + token;
        System.out.println("Gửi email reset về: " + resetLink);
        mailService.sendResetPasswordEmail(email, resetLink);
    }


    @Override
    public void resetPassword(String token, String newPassword) {
        String redisKey = "reset:" + token;
        String userIdStr = jedis.get(redisKey);

        if (userIdStr == null) {
            throw new RuntimeException("Token không hợp lệ hoặc đã hết hạn");
        }

        Long userId = Long.parseLong(userIdStr);
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setPassword(new UserPassword(passwordEncoder.encode(newPassword)));
        userRepositoryPort.save(user);

        jedis.del(redisKey); // Xóa token sau khi dùng
    }


    @Override
    @Transactional
    public UserAuthDTO loginWithGoogle(OidcUser oidcUser) {
        String email = oidcUser.getEmail();
        if (email == null) {
            throw new RuntimeException("Email not found in Google profile");
        }

        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);

        // Nếu người dùng chưa tồn tại, tạo mới
        if (user == null) {
            registerService.registerGoogleUser(
                oidcUser.getFullName() != null ? oidcUser.getFullName() : email, 
                email
            );
            user = userRepositoryPort.findByEmail(userEmail);
        }
        else if (!user.getAuthProvider().contains("GOOGLE")) {
            // Nếu email đã tồn tại nhưng chưa liên kết với Google
            // throw new RuntimeException("Email already registered with email/password. Please link your Google account.");
            user.setAuthProvider(user.getAuthProvider() + ",GOOGLE");
            userRepositoryPort.save(user);
        }

    // Tạo access token
    String accessToken = jwtUtil.generateToken(email);
    if (user.getUserAuths() == null) {
        user.setUserAuths(new ArrayList<>());
    }
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

    userRepositoryPort.save(user);

    return UserAuthMapper.toDTO(userAuth, accessToken);
    }


    @Override
    public boolean isEmailVerify(String email) {
        UserEmail userEmail = new UserEmail(email);
        boolean isVerified = userRepositoryPort.findByEmail(userEmail) != null;
        return isVerified;
    }
}
