package com.app.Music_Web.API.Controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.app.Music_Web.API.Request.RoleRequest;
import com.app.Music_Web.API.Response.RoleResponse;
import com.app.Music_Web.Application.DTO.RoleDTO;
import com.app.Music_Web.Application.Ports.In.Role.DeleteRoleService;
import com.app.Music_Web.Application.Ports.In.Role.FindRoleService;
import com.app.Music_Web.Application.Ports.In.Role.SaveRoleService;
import com.app.Music_Web.Application.Ports.In.Role.UpdateRoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final FindRoleService findRoleService;
    private final SaveRoleService saveRoleService;
    private final UpdateRoleService updateRoleService;
    private final DeleteRoleService deleteRoleService;
    public RoleController(
        FindRoleService findRoleService, 
        SaveRoleService saveRoleService,
        UpdateRoleService updateRoleService,
        DeleteRoleService deleteRoleService) {
        this.findRoleService = findRoleService;
        this.saveRoleService = saveRoleService;
        this.deleteRoleService=deleteRoleService;
        this.updateRoleService=updateRoleService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<RoleResponse>> getListAllRoles() {
        List<RoleDTO> roles = findRoleService.findAll();
        List<RoleResponse> roleResponse =roles.stream()
                    .map(role-> RoleResponse.builder()
                        .roleId(role.getRoleId())
                        .roleName(role.getRoleName())
                        .description(role.getDescription())
                        .build())
                    .toList();
        return ResponseEntity.ok(roleResponse);
        
    }

    @PostMapping
    public ResponseEntity<Void> createRole(@RequestBody RoleRequest request) {
        saveRoleService.saveRole(request.getRoleName(), 
                            request.getDescription(),
                            request.getPermissionNames());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<RoleResponse>> getPageAllRoles(
                                        @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.unsorted());
        Page<RoleDTO> roles = findRoleService.findAllWithPermissions(pageable);
        Page<RoleResponse> roleResponses =roles.map(role-> RoleResponse.builder()
                                            .roleId(role.getRoleId())
                                            .roleName(role.getRoleName())
                                            .description(role.getDescription())
                                            .permissions(role.getPermissions())
                                            .build());
        return ResponseEntity.ok(roleResponses);
    }


    @DeleteMapping("delete/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId){
        deleteRoleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

    //tìm kiếm gần đúng với phân trang
    @GetMapping("/search")
    public ResponseEntity<Page<RoleResponse>> searchRolesFuzzy(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        Page<RoleDTO> roles = findRoleService.searchByRoleName(keyword, pageable);
        Page<RoleResponse> roleResponses = roles.map(role -> RoleResponse.builder()
                .roleId(role.getRoleId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .permissions(role.getPermissions())
                .build());
        return ResponseEntity.ok(roleResponses);
    }


    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long roleId){
        RoleDTO role= findRoleService.findRoleById(roleId);
        RoleResponse roleResponse= RoleResponse.builder()
                                    .roleId(roleId)
                                    .roleName(role.getRoleName())
                                    .description(role.getDescription())
                                    .permissions(role.getPermissions())
                                    .build();
        return ResponseEntity.ok(roleResponse);
    }

    @PutMapping("/update/{roleId}")
    public ResponseEntity<Void> updateRole(
            @PathVariable Long roleId,
            @RequestBody RoleRequest request) {
        updateRoleService.updateRole(
                roleId,
                request.getRoleName(),
                request.getDescription(),
                request.getPermissionNames());
        return ResponseEntity.ok().build();
    }
}
