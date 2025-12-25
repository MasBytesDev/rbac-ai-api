package com.masbytes.rbacapi.role.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new role. Contains the role name and description
 * with validation constraints.
 */
public record CreateRoleRequest(
        
        /**
         * The unique name of the role. Must start with 'ROLE_', contain only
         * uppercase letters and underscores, and be between 10 and 75
         * characters long.
         */
        @NotBlank(message = "Role name is required")
        @Pattern(
                regexp = "^ROLE_[A-Z]+(_[A-Z]+)*$",
                message = "Role name must start with 'ROLE_' and contain only uppercase letters and underscores"
        )
        @Size(min = 10, max = 75, message = "Role name must be between 10 and 75 characters")
        String roleName,
        
        /**
         * A brief description of the role. Must not be blank and must not
         * exceed 100 characters.
         */
        @NotBlank(message = "Role description is required")
        @Size(max = 100, message = "Description must not exceed 100 characters")
        String roleDescription
        
        ) {

}
