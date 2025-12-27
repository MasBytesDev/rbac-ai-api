package com.masbytes.rbacapi.role.infrastructure.rest;

import com.masbytes.rbacapi.role.domain.service.RoleService;
import com.masbytes.rbacapi.role.domain.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for managing roles. Provides endpoints to create, retrieve,
 * update description, and change status of roles.
 */
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * Creates a new role.
     *
     * @param request the request containing role details
     * @return ResponseEntity with the created role and HTTP 201 status
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(request));
    }

    /**
     * Retrieves a role by its public UUID.
     *
     * @param publicId the public identifier of the role
     * @return ResponseEntity with the role and HTTP 200 status
     */
    @GetMapping("/{publicId}")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<RoleResponse> getByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(roleService.getRoleByPublicId(publicId));
    }

    /**
     * Updates the description of an existing role.
     *
     * @param publicId the public identifier of the role
     * @param request the request containing the new description
     * @return ResponseEntity with the updated role and HTTP 200 status
     * @throws IllegalArgumentException if the description is invalid
     */
    @PatchMapping("/{publicId}/description")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<RoleResponse> updateDescription(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpdateRoleDescriptionRequest request) {
        return ResponseEntity.ok(roleService.updateDescription(publicId, request));
    }

    /**
     * Changes the status of an existing role.
     *
     * @param publicId the public identifier of the role
     * @param request the request containing the new status
     * @return ResponseEntity with the updated role and HTTP 200 status
     */
    @PatchMapping("/{publicId}/status")
    @PreAuthorize("hasAuthority('ROLE_MANAGE')")
    public ResponseEntity<RoleResponse> changeStatus(
            @PathVariable UUID publicId,
            @Valid @RequestBody ChangeRoleStatusRequest request) {
        return ResponseEntity.ok(roleService.changeStatus(publicId, request));
    }
}
