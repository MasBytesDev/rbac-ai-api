package com.masbytes.rbacapi.appuser.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.InvalidEntityStateException;

/**
 * Exception thrown when an application user has an invalid format.
 * <p>
 * Used to indicate that the provided user data does not meet the required
 * structural or validation rules. Includes a standardized error code for
 * consistency across the system.
 */
public class InvalidAppUserFormatException extends InvalidEntityStateException {

    /**
     * Constructs a new exception with a custom validation message.
     *
     * @param message detailed description of the invalid format issue
     */
    public InvalidAppUserFormatException(String message) {

        super(message, "INVALID_USER_FORMAT");
    }
}
