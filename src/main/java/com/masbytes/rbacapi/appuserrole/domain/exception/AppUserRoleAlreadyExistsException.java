package com.masbytes.rbacapi.appuserrole.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.EntityAlreadyExistsException;
import java.util.UUID;

/**
 * Exception thrown when a user already has a specific role assigned. Provides a
 * standardized error code for duplicate role assignment.
 */
public class AppUserRoleAlreadyExistsException extends EntityAlreadyExistsException {

    /**
     * Constructs a new exception indicating that the given user already has the
     * specified role assigned.
     *
     * @param userPublicId the public UUID of the user
     * @param rolePublicId the public UUID of the role
     */
    public AppUserRoleAlreadyExistsException(UUID userPublicId, UUID rolePublicId) {
        super(
                String.format("User [%s] already has the role [%s] assigned", userPublicId, rolePublicId),
                "USER_ROLE_ALREADY_EXISTS"
        );
    }
}
