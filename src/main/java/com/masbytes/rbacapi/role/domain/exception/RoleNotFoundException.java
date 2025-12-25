package com.masbytes.rbacapi.role.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.EntityNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when a role cannot be found in the system. Provides a
 * standardized error code for missing roles.
 */
public class RoleNotFoundException extends EntityNotFoundException {

    private static final String CODE = "ROLE_NOT_FOUND";

    /**
     * Constructs a new exception indicating that a role with the given public
     * UUID could not be found.
     *
     * @param publicId the public identifier of the role
     */
    public RoleNotFoundException(UUID publicId) {
        super(String.format("Role not found with ID: %s", publicId), CODE);
    }

    /**
     * Constructs a new exception indicating that a role with the given name
     * could not be found.
     *
     * @param name the name of the missing role
     */
    public RoleNotFoundException(String name) {
        super(String.format("Role not found with name: %s", name), CODE);
    }

}
