package com.app.Music_Web.Application.Mapper;

import com.app.Music_Web.Application.DTO.UserAuthDTO;
import com.app.Music_Web.Domain.Entities.UserAuth;

public class UserAuthMapper {
    public static UserAuthDTO toDTO(UserAuth userAuth, String accessToken) {
        return UserAuthDTO.builder()
                .authId(userAuth.getAuthId())
                .accessToken(accessToken)
                .refreshToken(userAuth.getRefreshToken())
                .refreshTokenExpiry(userAuth.getRefreshTokenExpiry())
                .userName(userAuth.getUser().getUserName().getUserName())
                .build();
    }
}