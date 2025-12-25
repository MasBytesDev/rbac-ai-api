package com.masbytes.rbacapi.permission.domain.exception;

import com.masbytes.rbacapi.shared.domain.enums.Status;
import com.masbytes.rbacapi.shared.domain.exception.InvalidEntityStateException;

/**
 * Exception thrown when an invalid transition between permission statuses is
 * attempted. Provides a standardized error code for disallowed status changes.
 */
public class InvalidPermissionStatusTransitionException extends InvalidEntityStateException {

    private static final String CODE = "INVALID_PERMISSION_STATUS_TRANSITION";

    /**
     * Constructs a new exception indicating that a permission status cannot
     * transition from one state to another.
     *
     * @param from the current status
     * @param to the attempted new status
     */
    public InvalidPermissionStatusTransitionException(Status from, Status to) {
        super(String.format("Cannot transition permission status from %s to %s", from, to), CODE);
    }
}
