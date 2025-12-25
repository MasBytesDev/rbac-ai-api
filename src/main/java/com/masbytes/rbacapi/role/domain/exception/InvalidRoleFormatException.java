package com.masbytes.rbacapi.role.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.InvalidEntityStateException;

/**
 * Exception thrown when a role format is invalid. Provides a standardized error
 * code for format violations, including naming convention mismatches.
 */
public class InvalidRoleFormatException extends InvalidEntityStateException {

    private static final String CODE = "INVALID_ROLE_FORMAT";

    /**
     * Constructs a new exception with a detailed message describing why the
     * role format is invalid.
     *
     * @param details explanation of the invalid format
     */
    public InvalidRoleFormatException(String details) {
        super(String.format("The role format is invalid: %s", details), CODE);
    }

    /**
     * Constructs a new exception indicating that a specific field contains a
     * value that does not meet naming conventions.
     *
     * @param field the field name being validated
     * @param value the invalid value provided
     */
    public InvalidRoleFormatException(String field, String value) {
        super(String.format("Value [%s] for field [%s] does not meet the naming conventions", value, field), CODE);
    }

}
