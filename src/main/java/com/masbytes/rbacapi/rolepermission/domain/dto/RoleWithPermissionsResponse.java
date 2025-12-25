package com.masbytes.rbacapi.rolepermission.domain.dto;

import java.util.List;
import java.util.UUID;

/**
 * Response DTO representing a role along with its associated permissions.
 * Includes the role identifier, name, status, and a list of permission
 * summaries.
 */
public record RoleWithPermissionsResponse(
        
        /**
         * The public UUID of the role.
         */
        UUID publicId,
        
        /**
         * The unique name of the role.
         */
        String roleName,
        
        /**
         * The current status of the role (e.g., ACTIVE, PENDING, DISABLED).
         */
        String roleStatus,
        
        /**
         * The list of permissions associated with the role, represented as
         * summarized permission responses.
         */
        List<PermissionSummaryResponse> permissions
        
        ) {

}
