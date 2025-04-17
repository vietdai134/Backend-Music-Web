package com.app.Music_Web.Application.Ports.In.Auth;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.app.Music_Web.Application.DTO.UserAuthDTO;

public interface AuthService {
    UserAuthDTO login (String email, String password);
    UserAuthDTO refreshAccessToken(String refreshToken);
    UserAuthDTO loginWithGoogle(OidcUser oidcUser);
}
