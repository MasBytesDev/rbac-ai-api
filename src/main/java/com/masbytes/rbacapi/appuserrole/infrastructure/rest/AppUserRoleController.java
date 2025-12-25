package com.masbytes.rbacapi.appuserrole.infrastructure.rest;

import com.masbytes.rbacapi.appuserrole.domain.service.AppUserRoleService;
import com.masbytes.rbacapi.appuserrole.domain.dto.AppUserRoleResponse;
import com.masbytes.rbacapi.appuserrole.domain.dto.AssignRoleRequest;
import com.masbytes.rbacapi.appuserrole.domain.dto.UpdateUserRoleRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing role assignments of application users. Provides
 * endpoints to assign, update, and revoke user roles.
 */
@RestController
@RequestMapping("/api/v1/app-users")
@RequiredArgsConstructor
public class AppUserRoleController {

    private final AppUserRoleService appUserRoleService;

    /**
     * Assigns a new role to a user.
     *
     * @param request the request containing user and role identifiers
     * @return ResponseEntity with the created role assignment and HTTP 201
     * status
     */
    @PostMapping("/roles")
    public ResponseEntity<AppUserRoleResponse> assignRole(
            @Valid @RequestBody AssignRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appUserRoleService.assignRole(request));
    }

    /**
     * Updates a user's role assignment by replacing an old role with a new one.
     *
     * @param userPublicId the public UUID of the user
     * @param request the request containing old and new role identifiers
     * @return ResponseEntity with the updated role assignment and HTTP 200
     * status
     */
    @PatchMapping("/{userPublicId}/roles")
    public ResponseEntity<AppUserRoleResponse> updateRole(
            @PathVariable UUID userPublicId,
            @Valid @RequestBody UpdateUserRoleRequest request) {
        return ResponseEntity.ok(appUserRoleService.updateRole(userPublicId, request));
    }

    /**
     * Revokes (deletes) a role assignment from a user.
     *
     * @param userPublicId the public UUID of the user
     * @param rolePublicId the public UUID of the role
     */
    @DeleteMapping("/{userPublicId}/roles/{rolePublicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeRole(
            @PathVariable UUID userPublicId,
            @PathVariable UUID rolePublicId) {
        appUserRoleService.revokeRole(userPublicId, rolePublicId);
    }
}
