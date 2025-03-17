package com.app.Music_Web.Infrastructure.Persistence;


import com.app.Music_Web.Domain.Entities.RolePermission;
import com.app.Music_Web.Domain.Entities.User;
import com.app.Music_Web.Domain.Entities.UserRole;
import com.app.Music_Web.Domain.ValueObjects.User.UserEmail;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.RolePermissionRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserRepository;
import com.app.Music_Web.Infrastructure.Persistence.Repositories.UserRoleRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    
    public UserDetailsServiceImpl(UserRepository userRepository, UserRoleRepository userRoleRepository, RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEmail userEmail = new UserEmail(email);
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        List<UserRole> userRoles = userRoleRepository.findByUser(user);
        if (userRoles.isEmpty()) {
            throw new RuntimeException("User has no roles assigned");
        }

        List<GrantedAuthority> authorities = userRoles.stream()
            .peek(userRole -> System.out.println("User " + email + " has role: " + userRole.getRole().getRoleName())) // In role
            .flatMap(userRole -> {
                List<RolePermission> rolePermissions = rolePermissionRepository.findByRole_RoleId(userRole.getRole().getRoleId());
                return rolePermissions.stream()
                    .map(rolePermission -> new SimpleGrantedAuthority(rolePermission.getPermission().getPermissionName()));
            })
            .distinct() // Loại bỏ trùng lặp nếu user có nhiều role với permission chung
            .collect(Collectors.toList());
        System.out.println("Permissions for user " + email + ": " + authorities);
        // return org.springframework.security.core.userdetails.User.builder()
        //         .username(user.getEmail().getEmail())
        //         .password(user.getPassword().getPassword())
        //         .roles(user.getAccountType().name())
        //         .build();
        return new org.springframework.security.core.userdetails.User(
            user.getUserName().getUserName(), // Lấy từ UserName value object
            user.getPassword().getPassword(), // Lấy từ UserPassword value object
            authorities
        );
    }
}
