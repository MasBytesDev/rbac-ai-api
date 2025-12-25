package com.masbytes.rbacapi.appuser.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.InvalidEntityStateException;

/**
 * Exception thrown when an application user is in an invalid state.
 * <p>
 * Used to indicate that the current state of the user violates expected
 * business rules or lifecycle constraints. Includes a standardized error code
 * for consistency across the system.
 */
public class InvalidAppUserStateException extends InvalidEntityStateException {

    /**
     * Constructs a new exception with a custom message describing the invalid
     * state condition.
     *
     * @param message detailed description of the invalid state
     */
    public InvalidAppUserStateException(String message) {

        super(message, "INVALID_USER_STATE");
    }
}
