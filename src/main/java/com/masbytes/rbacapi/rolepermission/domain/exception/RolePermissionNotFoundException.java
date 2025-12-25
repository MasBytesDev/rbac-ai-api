package com.masbytes.rbacapi.rolepermission.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.EntityNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when the association between a role and a permission cannot
 * be found in the system. Provides a standardized error code for missing
 * role-permission links.
 */
public class RolePermissionNotFoundException extends EntityNotFoundException {

    /**
     * Constructs a new exception indicating that the association between the
     * specified role and permission does not exist.
     *
     * @param rolePublicId the public UUID of the role
     * @param permissionPublicId the public UUID of the permission
     */
    public RolePermissionNotFoundException(UUID rolePublicId, UUID permissionPublicId) {
        super(
                String.format("Association between Role [%s] and Permission [%s] was not found", rolePublicId, permissionPublicId),
                "ROLE_PERMISSION_NOT_FOUND"
        );
    }
}
