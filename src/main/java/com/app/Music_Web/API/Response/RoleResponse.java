package com.app.Music_Web.API.Response;

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
}
