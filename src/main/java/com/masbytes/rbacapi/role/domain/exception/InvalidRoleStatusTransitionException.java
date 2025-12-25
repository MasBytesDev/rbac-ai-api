package com.masbytes.rbacapi.role.domain.exception;

import com.masbytes.rbacapi.shared.domain.enums.Status;
import com.masbytes.rbacapi.shared.domain.exception.InvalidEntityStateException;

/**
 * Exception thrown when an invalid transition between role statuses is
 * attempted. Provides a standardized error code for disallowed status changes.
 */
public class InvalidRoleStatusTransitionException extends InvalidEntityStateException {

    private static final String CODE = "INVALID_ROLE_STATUS_TRANSITION";

    /**
     * Constructs a new exception indicating that a role status cannot
     * transition from one state to another.
     *
     * @param from the current role status
     * @param to the attempted new role status
     */
    public InvalidRoleStatusTransitionException(Status from, Status to) {
        super(String.format("Cannot transition role status from %s to %s", from, to), CODE);
    }

}
