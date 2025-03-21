package com.app.Music_Web.API.Response;

import java.util.List;

import com.app.Music_Web.Application.DTO.PermissionDTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleResponse {
    private Long roleId;
    private String roleName;
    private String description;

    private List<PermissionDTO> permissions;
}
