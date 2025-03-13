package com.app.Music_Web.API.Controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.PermissionRequest;
import com.app.Music_Web.API.Response.PermissionResponse;
import com.app.Music_Web.Application.DTO.PermissionDTO;
import com.app.Music_Web.Application.Ports.In.Permission.SavePermissionService;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private final SavePermissionService savePermissionService;
    public PermissionController(SavePermissionService savePermissionService) {
        this.savePermissionService = savePermissionService;
    }
    
    @PostMapping
    public PermissionResponse createPermission(@RequestBody PermissionRequest request) {
        PermissionDTO savedPermission = savePermissionService.savePermission(request.getPermissionName(), 
                            request.getDescription());
        return PermissionResponse.builder()
                .permissionId(savedPermission.getPermissionId())
                .permissionName(savedPermission.getPermissionName())
                .description(savedPermission.getDescription())
                .build();
    }
}
