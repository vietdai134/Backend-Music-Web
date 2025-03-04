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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "role_permission")
public class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_permission_id")
    private Long rolePermissionId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="assigned_date",nullable = false)
    private Date assignedDate;
    
    //khóa ngoại liên kết với bảng role
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    //khóa ngoại liên kết với bảng permission
    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
