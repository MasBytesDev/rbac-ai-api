package com.masbytes.rbacapi.rolepermission.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Request DTO for assigning a permission to a role. Contains the identifiers of
 * both the role and the permission.
 */
public record AssignPermissionRequest(
        
        /**
         * The public UUID of the role to which the permission will be assigned.
         * Must not be null.
         */
        @NotNull
        UUID rolePublicId,
        
        /**
         * The public UUID of the permission to assign. Must not be null.
         */
        @NotNull
        UUID permissionPublicId
        
        ) {

}
