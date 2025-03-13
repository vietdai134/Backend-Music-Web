package com.app.Music_Web.API.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PermissionRequest {
    private String permissionName;
    private String description;
}
