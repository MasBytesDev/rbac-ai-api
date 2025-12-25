package com.masbytes.rbacapi.permission.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for changing the status of a permission. Contains the new status
 * value to be applied.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChangePermissionStatusRequest(
        
        /**
         * The new status to assign to the permission. Must not be null.
         */
        @NotNull(message = "Permission Status is required")
        Status newStatus
        
        ) {

}
