package com.masbytes.rbacapi.appuserrole.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.EntityNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when a role assignment for a user cannot be found. Provides
 * a standardized error code for missing user-role associations.
 */
public class AppUserRoleNotFoundException extends EntityNotFoundException {

    /**
     * Constructs a new exception indicating that the given user does not have
     * the specified role assigned.
     *
     * @param userPublicId the public UUID of the user
     * @param rolePublicId the public UUID of the role
     */
    public AppUserRoleNotFoundException(UUID userPublicId, UUID rolePublicId) {
        super(
                String.format("Assignment not found: User [%s] does not have the role [%s]", userPublicId, rolePublicId),
                "USER_ROLE_NOT_FOUND"
        );
    }
}
