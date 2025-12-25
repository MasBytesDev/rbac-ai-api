package com.masbytes.rbacapi.permission.infrastructure.rest;

import com.masbytes.rbacapi.permission.domain.dto.*;
import com.masbytes.rbacapi.permission.domain.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing permissions. Provides endpoints to create,
 * retrieve, update description, and change status of permissions.
 */
@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * Creates a new permission.
     *
     * @param request the request containing permission details
     * @return ResponseEntity with the created permission and HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<PermissionResponse> create(
            @Valid @RequestBody CreatePermissionRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(permissionService.create(request));
    }

    /**
     * Retrieves a permission by its public UUID.
     *
     * @param publicId the public identifier of the permission
     * @return ResponseEntity with the permission and HTTP 200 status
     */
    @GetMapping("/{publicId}")
    public ResponseEntity<PermissionResponse> getByPublicId(
            @PathVariable UUID publicId) {
        return ResponseEntity.ok(permissionService.getByPublicId(publicId));
    }

    /**
     * Updates the description of an existing permission.
     *
     * @param publicId the public identifier of the permission
     * @param request the request containing the new description
     * @return ResponseEntity with the updated permission and HTTP 200 status
     */
    @PatchMapping("/{publicId}/description")
    public ResponseEntity<PermissionResponse> updateDescription(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpdatePermissionDescriptionRequest request) {
        return ResponseEntity.ok(permissionService.updateDescription(publicId, request));
    }

    /**
     * Changes the status of an existing permission.
     *
     * @param publicId the public identifier of the permission
     * @param request the request containing the new status
     * @return ResponseEntity with the updated permission and HTTP 200 status
     */
    @PatchMapping("/{publicId}/status")
    public ResponseEntity<PermissionResponse> changeStatus(
            @PathVariable UUID publicId,
            @Valid @RequestBody ChangePermissionStatusRequest request) {
        return ResponseEntity.ok(permissionService.changeStatus(publicId, request));
    }
}
