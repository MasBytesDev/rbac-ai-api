package com.masbytes.rbacapi.permission.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.EntityAlreadyExistsException;

/**
 * Exception thrown when attempting to create a permission that already exists
 * in the system. Provides a standardized error code for duplicate permissions.
 */
public class PermissionAlreadyExistsException extends EntityAlreadyExistsException {

    private static final String CODE = "PERMISSION_ALREADY_EXISTS";

    /**
     * Constructs a new exception indicating that a permission with the given
     * name already exists.
     *
     * @param name the name of the duplicate permission
     */
    public PermissionAlreadyExistsException(String name) {
        super(String.format("The permission with name [%s] already exists", name), CODE);
    }
}
