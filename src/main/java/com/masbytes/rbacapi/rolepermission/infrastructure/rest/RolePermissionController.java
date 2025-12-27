package com.masbytes.rbacapi.rolepermission.infrastructure.rest;

import com.masbytes.rbacapi.rolepermission.domain.service.RolePermissionService;
import com.masbytes.rbacapi.rolepermission.domain.dto.AssignPermissionRequest;
import com.masbytes.rbacapi.rolepermission.domain.dto.RolePermissionResponse;
import com.masbytes.rbacapi.rolepermission.domain.dto.RoleWithPermissionsResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for managing role-permission associations. Provides endpoints
 * to assign permissions to roles, revoke permissions, and retrieve roles with
 * their associated permissions.
 */
@RestController
@RequestMapping("/api/v1/role-permissions")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService service;

    /**
     * Assigns a permission to a role.
     *
     * @param request the request containing role and permission identifiers
     * @return ResponseEntity with the created role-permission association and
     * HTTP 201 status
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<RolePermissionResponse> assignPermission(
            @Valid @RequestBody AssignPermissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.assignPermissionToRole(request));
    }

    /**
     * Retrieves a role along with all its associated permissions.
     *
     * @param rolePublicId the public UUID of the role
     * @return ResponseEntity with the role and its permissions, and HTTP 200
     * status
     */
    @GetMapping("/roles/{rolePublicId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<RoleWithPermissionsResponse> getRoleWithPermissions(
            @PathVariable UUID rolePublicId) {
        return ResponseEntity.ok(service.getRoleWithPermissions(rolePublicId));
    }

    /**
     * Revokes a permission from a role by deleting the association.
     *
     * @param rolePublicId the public UUID of the role
     * @param permissionPublicId the public UUID of the permission
     * @return ResponseEntity with HTTP 204 status if the association is
     * successfully deleted
     */
    @DeleteMapping("/roles/{rolePublicId}/permissions/{permissionPublicId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<Void> revokePermission(
            @PathVariable UUID rolePublicId,
            @PathVariable UUID permissionPublicId) {
        service.revokePermissionFromRole(rolePublicId, permissionPublicId);
        return ResponseEntity.noContent().build();
    }
}
