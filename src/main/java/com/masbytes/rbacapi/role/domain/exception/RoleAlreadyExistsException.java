package com.masbytes.rbacapi.role.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.EntityAlreadyExistsException;

/**
 * Exception thrown when attempting to create a role that already exists in the
 * system. Provides a standardized error code for duplicate roles.
 */
public class RoleAlreadyExistsException extends EntityAlreadyExistsException {

    private static final String CODE = "ROLE_ALREADY_EXISTS";

    /**
     * Constructs a new exception indicating that a role with the given name
     * already exists.
     *
     * @param name the name of the duplicate role
     */
    public RoleAlreadyExistsException(String name) {
        super(String.format("The role with name [%s] already exists", name), CODE);
    }

}
