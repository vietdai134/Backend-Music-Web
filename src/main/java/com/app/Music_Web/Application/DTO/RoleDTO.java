package com.app.Music_Web.Application.DTO;
import java.util.List;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {
    private Long roleId;
    private String roleName;
    private String description;

    private List<PermissionDTO> permissions;
}
