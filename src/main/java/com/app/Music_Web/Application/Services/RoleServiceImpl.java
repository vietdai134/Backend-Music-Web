package com.app.Music_Web.Application.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.Music_Web.Application.DTO.RoleDTO;
import com.app.Music_Web.Application.Mapper.RoleMapper;
import com.app.Music_Web.Application.Ports.In.Role.DeleteRoleService;
import com.app.Music_Web.Application.Ports.In.Role.FindRoleService;
import com.app.Music_Web.Application.Ports.In.Role.SaveRoleService;
import com.app.Music_Web.Application.Ports.In.Role.UpdateRoleService;
import com.app.Music_Web.Application.Ports.Out.PermissionRepositoryPort;
import com.app.Music_Web.Application.Ports.Out.RoleRepositoryPort;
import com.app.Music_Web.Domain.Entities.Permission;
import com.app.Music_Web.Domain.Entities.Role;
import com.app.Music_Web.Domain.Entities.RolePermission;

@Service
public class RoleServiceImpl implements SaveRoleService, FindRoleService,DeleteRoleService, UpdateRoleService {
    private final RoleRepositoryPort roleRepositoryPort;
    private final PermissionRepositoryPort permissionRepositoryPort;

    public RoleServiceImpl (
        RoleRepositoryPort roleRepositoryPort,
        PermissionRepositoryPort permissionRepositoryPort) {
        this.roleRepositoryPort = roleRepositoryPort;
        this.permissionRepositoryPort=permissionRepositoryPort;
    }

    @Override
    public RoleDTO saveRole(String roleName, String roleDescription,List<String> permissionNames) {
        Role role = Role.builder()
                        .roleName(roleName)
                        .description(roleDescription)
                        .rolePermissions(new ArrayList<>())
                        .build();
        List<Permission> permissions= permissionRepositoryPort.findByPermissionNameIn(permissionNames);
        List<RolePermission> rolePermissions= permissions.stream()
                                    .map(
                                        permission -> RolePermission.builder()
                                        .role(role)
                                        .permission(permission)
                                        .assignedDate(new Date())
                                        .build())
                                    .collect(Collectors.toList());
        role.getRolePermissions().addAll(rolePermissions);

        Role savedRole = roleRepositoryPort.save(role);
        return RoleMapper.toDTO(savedRole);
    }

    @Override
    public List<RoleDTO> findAll() {
        List<Role> roles = roleRepositoryPort.findAll();
        return roles.stream()
                    .map(RoleMapper::toDTO)
                    .toList();
    }

    @Override
    public RoleDTO findByRoleName(String roleName) {
        Role role = roleRepositoryPort.findByRoleName(roleName);
        if(role==null){
            throw new RuntimeException("Role not found");
        }
        return RoleMapper.toDTO(role);
    }

    @Override
    public List<RoleDTO> findByRoleNameIn(List<String> roleName) {
        List<Role> role = roleRepositoryPort.findByRoleNameIn(roleName);
        if(role == null){
            throw new RuntimeException("Role not found");
        }
        // return RoleMapper.toDTO(role);
        return role.stream()
                    .map(RoleMapper::toDTO)
                    .toList();
    }

    @Override
    public Page<RoleDTO> findAllWithPermissions(Pageable pageable) {
        Page<Role> roles= roleRepositoryPort.findAllWithPermissions(pageable);
        return roles.map(RoleMapper::toDTO);
    }

    @Override
    public RoleDTO updateRole(Long roleId, String roleName, String description, List<String> permissionNames) {
       Role role=roleRepositoryPort.findById(roleId)
        .orElseThrow(()->new RuntimeException("Khong tim thay role"));
       role.setRoleName(roleName);
       role.setDescription(description);
       role.getRolePermissions().clear();
       List<Permission> permissions= permissionRepositoryPort.findByPermissionNameIn(permissionNames);
       List<RolePermission> rolePermissions=permissions.stream()
        .map(permission -> RolePermission.builder()
        .role(role)
        .permission(permission)
        .assignedDate(new Date())
        .build())
        .collect(Collectors.toList());
        role.getRolePermissions().addAll(rolePermissions);

        Role updatedRole= roleRepositoryPort.save(role);
        return RoleMapper.toDTO(updatedRole);
    }

    @Override
    public void deleteRole(Long roleId) {
        Role role = roleRepositoryPort.findById(roleId)
            .orElseThrow(()-> new RuntimeException("Khong tim thay role"));
        roleRepositoryPort.delete(role);
    }

    @Override
    public Page<RoleDTO> searchByRoleName(String keyword, Pageable pageable) {
       Page<Role> roles= roleRepositoryPort.searchByRoleName(keyword, pageable);
       return roles.map(RoleMapper::toDTO);
    }

    @Override
    public RoleDTO findRoleById(Long roleId) {
        Role role=roleRepositoryPort.findById(roleId)
        .orElseThrow(()->new RuntimeException("Khong tim thay role"));
        return RoleMapper.toDTO(role);
    }
    

    
}
