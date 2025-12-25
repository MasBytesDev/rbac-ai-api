package com.masbytes.rbacapi.appuser.domain.exception;

import com.masbytes.rbacapi.shared.domain.exception.EntityNotFoundException;
import java.util.UUID;

/**
 * Exception thrown when an application user cannot be found.
 * <p>
 * Provides constructors to indicate missing users either by their public ID
 * or by their email address. Uses a standardized error code for consistency.
 */
public class AppUserNotFoundException extends EntityNotFoundException {
    
     /**
     * Constructs a new exception indicating that a user with the given public ID was not found.
     *
     * @param publicId the unique identifier of the user
     */
    public AppUserNotFoundException(UUID publicId) {

        super(String.format("User with ID [%s] was not found", publicId), "USER_NOT_FOUND");
    }

     /**
     * Constructs a new exception indicating that a user with the given email was not found.
     *
     * @param email the email address of the user
     */
    public AppUserNotFoundException(String email) {

        super(String.format("User with email [%s] was not found", email), "USER_NOT_FOUND");
    }
}