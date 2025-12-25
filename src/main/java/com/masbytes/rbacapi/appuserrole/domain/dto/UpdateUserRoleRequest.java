package com.masbytes.rbacapi.appuserrole.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Request DTO for updating a user's role assignment. Contains the old role UUID
 * and the new role UUID.
 */
public record UpdateUserRoleRequest(
        @NotNull(message = "Old Role Public ID is required")
        UUID oldRolePublicId,
        @NotNull(message = "New Role Public ID is required")
        UUID newRolePublicId
        ) {

}
