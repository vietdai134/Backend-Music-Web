package com.app.Music_Web.API.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.Music_Web.API.Request.RoleRequest;
import com.app.Music_Web.API.Request.SongRequest;
import com.app.Music_Web.API.Response.RoleResponse;
import com.app.Music_Web.API.Response.SongResponse;
import com.app.Music_Web.Application.DTO.RoleDTO;
import com.app.Music_Web.Application.DTO.SongDTO;
import com.app.Music_Web.Application.Ports.In.Role.FindRoleService;
import com.app.Music_Web.Application.Ports.In.Role.SaveRoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final FindRoleService findRoleService;
    private final SaveRoleService saveRoleService;
    public RoleController(FindRoleService findRoleService, SaveRoleService saveRoleService) {
        this.findRoleService = findRoleService;
        this.saveRoleService = saveRoleService;
    }

    @GetMapping("/all")
    public List<RoleResponse> getAllRoles() {
        List<RoleDTO> roles = findRoleService.findAll();
        return roles.stream()
                    .map(role-> RoleResponse.builder()
                        .roleId(role.getRoleId())
                        .roleName(role.getRoleName())
                        .description(role.getDescription())
                        .build())
                    .toList();
        
    }

    @PostMapping
    public RoleResponse createSong(@RequestBody RoleRequest request) {
        RoleDTO savedRole = saveRoleService.saveRole(request.getRoleName(), 
                            request.getDescription());
        return RoleResponse.builder()
                .roleId(savedRole.getRoleId())
                .roleName(savedRole.getRoleName())
                .description(savedRole.getDescription())
                .build();
    }

}
