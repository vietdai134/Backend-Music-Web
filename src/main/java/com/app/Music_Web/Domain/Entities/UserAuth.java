package com.app.Music_Web.Domain.Entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_auth")
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_id")
    private Long authId;

    @Column(nullable = true,name = "google_id")
    private String googleId;

    @Column(nullable = true,name = "facebook_id")
    private String facebookId;

    @Column(nullable = false,name="refresh_token")
    private String refreshToken;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false,name = "refresh_token_expiry")
    private Date refreshTokenExpiry;

    //khóa ngoại liên kết đến bảng User
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
