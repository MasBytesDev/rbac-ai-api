package com.masbytes.rbacapi.appuser.domain.dto;

import com.masbytes.rbacapi.shared.domain.enums.Status;
import jakarta.validation.constraints.NotNull;

/**
 * Request object for changing the status of an application user.
 * <p>
 * Ensures that a new status value is provided and validated before
 * processing the update operation.
 */
public record ChangeAppUserStatusRequest(

        
     /**
     * The new status to be assigned to the user.
     * Must not be null.
     */
    @NotNull(message = "New status is required")
    Status newStatus

        
) {}