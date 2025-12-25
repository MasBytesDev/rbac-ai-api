package com.masbytes.rbacapi.appuser.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request object for creating a new application user.
 * <p>
 * Contains the required fields for user registration, including
 * fullname, email, and password. Each field is validated to ensure
 * proper format and constraints before processing.
 */
public record CreateAppUserRequest(

     /**
     * Full name of the user.
     * Must include at least a first name and a last name,
     * only letters allowed, length between 3 and 100 characters.
     */
    @NotBlank(message = "Fullname is required")
    @Size(min = 3, max = 100, message = "Fullname must be between 3 and 100 characters")
    @Pattern(
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+(\\s[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)+$",
        message = "Fullname must contain at least a first name and a last name (only letters allowed)"
    )
    String fullname,

     /**
     * Email address of the user.
     * Must follow a valid email format and not exceed 100 characters.
     */
    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
        message = "Email must be a valid format (e.g., user@example.com)"
    )
    String email,

     /**
     * Password for the user account.
     * Must be between 8 and 255 characters.
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    String password
        
) {}