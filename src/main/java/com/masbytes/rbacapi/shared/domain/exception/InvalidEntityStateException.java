package com.masbytes.rbacapi.shared.domain.exception;

/**
 * Base exception for cases where an entity is in an invalid state. Extends
 * DomainException and provides a standardized way to represent illegal or
 * inconsistent state transitions across the domain layer.
 */
public abstract class InvalidEntityStateException extends DomainException {

    /**
     * Constructs a new InvalidEntityStateException with the specified message
     * and error code.
     *
     * @param message the detail message describing the invalid state
     * @param errorCode the application-specific error code
     */
    protected InvalidEntityStateException(String message, String errorCode) {
        super(message, errorCode);
    }

}
