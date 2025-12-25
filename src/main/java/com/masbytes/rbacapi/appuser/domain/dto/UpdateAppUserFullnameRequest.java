package com.masbytes.rbacapi.appuser.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request object for updating the fullname of an application user.
 * <p>
 * Validates that the provided fullname meets length and format requirements,
 * ensuring at least a first name and a last name are included.
 */
public record UpdateAppUserFullnameRequest(

     /**
     * Full name of the user.
     * Must be between 3 and 100 characters and contain
     * at least a first name and a last name (letters only).
     */
    @NotBlank(message = "Fullname is required")
    @Size(min = 3, max = 100, message = "Fullname must be between 3 and 100 characters")
    @Pattern(
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+(\\s[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)+$",
        message = "Fullname must contain at least a first name and a last name (only letters allowed)"
    )
    String fullname
        
) {}