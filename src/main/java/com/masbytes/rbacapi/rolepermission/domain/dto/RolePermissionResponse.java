package com.masbytes.rbacapi.rolepermission.domain.dto;

import java.util.UUID;

/**
 * Response DTO representing the association between a role and a permission.
 * Includes identifiers for both entities, their names, and the timestamp of
 * assignment.
 */
public record RolePermissionResponse(
        /**
         * The public UUID of the role-permission association itself.
         */
        UUID associationPublicId, // El UUID de la relación en sí

        /**
         * The public UUID of the associated role.
         */
        UUID rolePublicId,
        
        /**
         * The name of the associated role.
         */
        String roleName,
        
        /**
         * The public UUID of the associated permission.
         */
        UUID permissionPublicId,
        
        /**
         * The name of the associated permission.
         */
        String permissionName,
        
        /**
         * The timestamp when the permission was assigned to the role. Inherited
         * from BaseEntity and formatted as a string.
         */
        String assignedAt // Fecha de creación (heredada de BaseEntity)

        ) {

}
