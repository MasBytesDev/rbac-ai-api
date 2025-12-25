package com.masbytes.rbacapi.rolepermission.domain.dto;

import java.util.UUID;

/**
 * Response DTO providing a summarized view of a permission. Includes
 * identifier, name, and current status.
 */
public record PermissionSummaryResponse(
        
        /**
         * The public UUID of the permission.
         */
        UUID publicId,
        
        /**
         * The unique name of the permission.
         */
        String permissionName,
        
        /**
         * The current status of the permission (e.g., ACTIVE, PENDING,
         * DISABLED).
         */
        String permissionStatus
        
        ) {

}
