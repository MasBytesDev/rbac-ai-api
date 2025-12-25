package com.masbytes.rbacapi.role.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating the description of a role. Contains the new
 * description with validation constraints.
 */
public record UpdateRoleDescriptionRequest(
        
        /**
         * The new description for the role. Must not be blank, must start with
         * an uppercase letter, and must not exceed 100 characters.
         */
        @NotBlank(message = "New description is required")
        @Pattern(
                regexp = "^[A-Z][a-zA-Z0-9 ,.]*$",
                message = "Description must start with uppercase and contain only letters, numbers, spaces, commas, or periods"
        )
        @Size(max = 100, message = "Description must not exceed 100 characters")
        String newDescription
        
        ) {

}
