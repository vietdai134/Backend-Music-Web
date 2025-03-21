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
import com.app.Music_Web.API.Request.PermissionRequest;
import com.app.Music_Web.API.Response.PermissionResponse;
import com.app.Music_Web.Application.DTO.PermissionDTO;
import com.app.Music_Web.Application.Ports.In.Permission.DeletePermissionService;
import com.app.Music_Web.Application.Ports.In.Permission.FindPermissionService;
import com.app.Music_Web.Application.Ports.In.Permission.SavePermissionService;
import com.app.Music_Web.Application.Ports.In.Permission.UpdatePermissionService;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private final SavePermissionService savePermissionService;
    private final FindPermissionService findPermissionService;
    private final UpdatePermissionService updatePermissionService;
    private final DeletePermissionService deletePermissionService;
    
    public PermissionController(
            SavePermissionService savePermissionService,
            FindPermissionService findPermissionService,
            UpdatePermissionService updatePermissionService,
            DeletePermissionService deletePermissionService
            ) {
        this.savePermissionService = savePermissionService;
        this.findPermissionService=findPermissionService;
        this.deletePermissionService=deletePermissionService;
        this.updatePermissionService=updatePermissionService;
    }
    @GetMapping("/list")
    public ResponseEntity<List<PermissionResponse>> getListAllPermissions() {
        List<PermissionDTO> permissions = findPermissionService.findAllPermissions();
        List<PermissionResponse> permissionResponses =permissions.stream()
                    .map(permission-> PermissionResponse.builder()
                        .permissionId(permission.getPermissionId())
                        .permissionName(permission.getPermissionName())
                        .description(permission.getDescription())
                        .build())
                    .toList();
        return ResponseEntity.ok(permissionResponses);
        
    }

    @GetMapping("/all")
    public ResponseEntity<Page<PermissionResponse>> getPageAllPermissions(
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size,Sort.unsorted());
        Page<PermissionDTO> permissions = findPermissionService.findAll(pageable);
        Page<PermissionResponse> permissionResponses = permissions.map(permission -> 
            PermissionResponse.builder()
                         .permissionId(permission.getPermissionId())
                         .permissionName(permission.getPermissionName())
                         .description(permission.getDescription())
                         .build()
        );

        return ResponseEntity.ok(permissionResponses);
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



    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable Long permissionId){
        PermissionDTO permission= findPermissionService.findByPermissionId(permissionId);
        PermissionResponse permissionResponse= PermissionResponse.builder()
                                    .permissionId(permissionId)
                                    .permissionName(permission.getPermissionName())
                                    .description(permission.getDescription())
                                    .build();
        return ResponseEntity.ok(permissionResponse);
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long permissionId){
        deletePermissionService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{permissionId}")
    public ResponseEntity<Void> updatePermission(
            @PathVariable Long permissionId,
            @RequestBody PermissionRequest request) {
        updatePermissionService.updatePermission(
                permissionId,
                request.getPermissionName(),
                request.getDescription());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PermissionResponse>> searchPermissionsFuzzy(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
        Page<PermissionDTO> permissions = findPermissionService.searchByPermissionName(keyword, pageable);
        Page<PermissionResponse> permissionResponse = permissions.map(permission -> PermissionResponse.builder()
                .permissionId(permission.getPermissionId())
                .permissionName(permission.getPermissionName())
                .description(permission.getDescription())
                .build());
        return ResponseEntity.ok(permissionResponse);
    }
}
