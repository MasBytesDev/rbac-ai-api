package com.masbytes.rbacapi.appuser.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.EntityAlreadyExistsException;

/**
 * Exception thrown when attempting to register an email that already exists in
 * the system.
 * <p>
 * Provides a standardized error code to indicate duplicate email registration.
 */
public class EmailAlreadyExistsException extends EntityAlreadyExistsException {

     /**
     * Constructs a new exception indicating that the given email is already registered.
     *
     * @param email the email address that caused the conflict
     */
    public EmailAlreadyExistsException(String email) {

        super(String.format("Email [%s] is already registered in the system", email), "EMAIL_ALREADY_EXISTS");
    }
}
