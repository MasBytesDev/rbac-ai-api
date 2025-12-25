package com.masbytes.rbacapi.appuserrole.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Request DTO for assigning a role to a user. Contains the user's public UUID
 * and the role's public UUID.
 */
public record AssignRoleRequest(
        @NotNull(message = "User Public ID is required")
        UUID userPublicId,
        @NotNull(message = "Role Public ID is required")
        UUID rolePublicId
        ) {

}
