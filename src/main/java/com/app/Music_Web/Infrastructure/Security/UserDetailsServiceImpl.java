package com.app.Music_Web.Infrastructure.Security;

import com.app.Music_Web.Application.Ports.Out.UserRepositoryPort;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Xóa import không cần thiết
// import org.springframework.security.core.userdetails.User as SpringUser;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepositoryPort.findByEmail(userEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // Sử dụng tên đầy đủ để tránh xung đột
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail().getEmail())
                .password(user.getPassword().getPassword())
                .roles(user.getAccountType().name())
                .build();
    }
}
