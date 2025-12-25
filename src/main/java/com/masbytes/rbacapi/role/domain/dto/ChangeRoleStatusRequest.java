package com.masbytes.rbacapi.role.domain.dto;

import com.masbytes.rbacapi.shared.domain.enums.Status;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for changing the status of a role. Contains the new status value
 * to be applied.
 */
public record ChangeRoleStatusRequest(
        
        /**
         * The new status to assign to the role. Must not be null.
         */
        @NotNull(message = "New status is required")
        Status newStatus
        
        ) {

}
