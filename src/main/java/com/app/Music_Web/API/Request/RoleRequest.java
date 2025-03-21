package com.app.Music_Web.API.Request;

import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {
    private String roleName;
    private String description;
    private List<String> permissionNames;
}
