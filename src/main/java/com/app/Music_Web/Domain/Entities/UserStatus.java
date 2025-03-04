package com.app.Music_Web.Domain.Entities;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "user_status")
public class UserStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false,name = "last_login")
    private Date lastLogin;

    @Column(nullable = false,name = "is_active")
    private boolean isActive;
    
    //khóa ngoại liên kết đến bảng User
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
