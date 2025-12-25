package com.masbytes.rbacapi.shared.domain.exception;

/**
 * Base exception for cases where an entity cannot be found in the system.
 * Extends DomainException and provides a standardized way to represent missing
 * entity errors across the domain layer.
 */
public abstract class EntityNotFoundException extends DomainException {

    /**
     * Constructs a new EntityNotFoundException with the specified message and
     * error code.
     *
     * @param message the detail message describing the missing entity
     * @param errorCode the application-specific error code
     */
    protected EntityNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }

}
