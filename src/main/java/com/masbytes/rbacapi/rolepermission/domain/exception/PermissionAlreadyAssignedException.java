package com.masbytes.rbacapi.rolepermission.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.EntityAlreadyExistsException;

/**
 * Exception thrown when attempting to assign a permission that is already
 * linked to a role. Provides a standardized error code for duplicate permission
 * assignments.
 */
public class PermissionAlreadyAssignedException extends EntityAlreadyExistsException {

    /**
     * Constructs a new exception indicating that the specified permission is
     * already assigned to the given role.
     *
     * @param roleName the name of the role
     * @param permissionName the name of the permission
     */
    public PermissionAlreadyAssignedException(String roleName, String permissionName) {
        super(
                String.format("Permission [%s] is already assigned to role [%s]", permissionName, roleName),
                "PERMISSION_ALREADY_ASSIGNED"
        );
    }
}
