package com.masbytes.rbacapi.permission.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a new permission. Contains the permission name and
 * description with validation constraints.
 */
public record CreatePermissionRequest(
        
        /**
         * The unique name of the permission. Must be uppercase, follow the
         * WORD_WORD format, and be 7–75 characters long.
         */
        @Pattern(
                regexp = "^[A-Z]+(_[A-Z]+)+$",
                message = "Permission name must be in UPPERCASE and follow the format WORD_WORD (e.g., USER_READ, ROLE_CREATE_ADMIN)"
        )
        @NotBlank(message = "Permission name is required")
        @Size(min = 7, max = 75, message = "Permission name must be between 7 to 75 characters long")
        String permissionName,
        
        /**
         * A brief description of the permission. Must start with an uppercase
         * letter, contain only valid characters, and be up to 100 characters
         * long.
         */
        @Pattern(regexp = "^[A-Z][a-zA-Z0-9 ,.]*$", message = "Description must start with uppercase and contain only letters, numbers, spaces, commas, or periods")
        @Size(max = 100, message = "Permission description must be up to 100 characters long")
        String permissionDescription
        
        ) {

}
