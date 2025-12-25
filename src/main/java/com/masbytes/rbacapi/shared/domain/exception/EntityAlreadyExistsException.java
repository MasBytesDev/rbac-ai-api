package com.masbytes.rbacapi.shared.domain.exception;

/**
 * Base exception for cases where an entity already exists in the system.
 * Extends DomainException and provides a standardized way to represent
 * duplicate entity errors across the domain layer.
 */
public abstract class EntityAlreadyExistsException extends DomainException {

    /**
     * Constructs a new EntityAlreadyExistsException with the specified message
     * and error code.
     *
     * @param message the detail message describing the duplicate entity error
     * @param errorCode the application-specific error code
     */
    protected EntityAlreadyExistsException(String message, String errorCode) {
        super(message, errorCode);
    }

}
