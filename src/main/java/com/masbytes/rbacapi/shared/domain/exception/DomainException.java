package com.masbytes.rbacapi.shared.domain.exception;

/**
 * Base class for all domain-specific exceptions. Extends RuntimeException and
 * adds an application-specific error code to provide standardized error
 * handling across the domain layer.
 */
public abstract class DomainException extends RuntimeException {

    /**
     * The application-specific error code associated with the exception. Used
     * to identify and categorize domain errors consistently.
     */
    private final String errorCode;

    /**
     * Constructs a new DomainException with the specified message and error
     * code.
     *
     * @param message the detail message describing the exception
     * @param errorCode the application-specific error code
     */
    protected DomainException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Returns the application-specific error code associated with this
     * exception.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
