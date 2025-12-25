package com.masbytes.rbacapi.permission.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.InvalidEntityStateException;

/**
 * Exception thrown when a permission format is invalid. Provides a standardized
 * error code for format violations, including naming convention mismatches.
 */
public class InvalidPermissionFormatException extends InvalidEntityStateException {

    private static final String CODE = "INVALID_PERMISSION_FORMAT";

    /**
     * Constructs a new exception with a detailed message describing why the
     * permission format is invalid.
     *
     * @param details explanation of the invalid format
     */
    public InvalidPermissionFormatException(String details) {
        super(String.format("The permission format is invalid: %s", details), CODE);
    }

    /**
     * Constructs a new exception indicating that a specific field contains a
     * value that does not meet naming conventions.
     *
     * @param field the field name being validated
     * @param value the invalid value provided
     */
    public InvalidPermissionFormatException(String field, String value) {
        super(String.format("Value [%s] for field [%s] does not meet the naming conventions", value, field), CODE);
    }
}
