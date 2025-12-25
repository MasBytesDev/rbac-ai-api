package com.masbytes.rbacapi.permission.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for updating the description of a permission. Contains the new
 * description with validation constraints.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdatePermissionDescriptionRequest(
        
        /**
         * The new description for the permission. Must start with an uppercase
         * letter, contain only valid characters, and be up to 100 characters
         * long.
         */
        @Pattern(regexp = "^[A-Z][a-zA-Z0-9 ,.]*$",
                message = "Description must start with uppercase and contain only letters, numbers, spaces, commas, or periods")
        @Size(max = 100, message = "Permission description must be up to 100 characters long")
        String newDescription
        
        ) {

}
