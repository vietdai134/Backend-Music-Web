package com.app.Music_Web.Domain.Entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name="role_name",nullable = false,unique = true)
    private String roleName;

    @Column(name="description",nullable = false)
    private String description;
    
    //liên kết 1-n với bảng userRole
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<UserRole> userRole;

    //liên kết 1-n với bảng rolePermission
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<RolePermission> rolePermissions;
}
