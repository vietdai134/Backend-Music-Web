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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id")
    private Long userRoleId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false,name = "granted_date")
    private Date grantedDate;

    //khóa ngoại liên kết với bảng user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    //khóa ngoại liên kết với bảng role
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
