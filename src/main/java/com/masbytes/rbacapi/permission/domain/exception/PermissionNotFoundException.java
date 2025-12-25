package com.masbytes.rbacapi.permission.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.EntityNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when a permission cannot be found in the system. Provides a
 * standardized error code for missing permissions.
 */
public class PermissionNotFoundException extends EntityNotFoundException {

    // Definimos el código único para este error
    private static final String CODE = "PERMISSION_NOT_FOUND";

    /**
     * Constructs a new exception indicating that a permission with the given
     * public UUID could not be found.
     *
     * @param publicId the public identifier of the permission
     */
    public PermissionNotFoundException(UUID publicId) {
        super(String.format("Permission not found with ID: %s", publicId), CODE);
    }

    /**
     * Constructs a new exception indicating that a permission with the given
     * name could not be found.
     *
     * @param name the name of the missing permission
     */
    public PermissionNotFoundException(String name) {
        super(String.format("Permission not found with name: %s", name), CODE);
    }
}
