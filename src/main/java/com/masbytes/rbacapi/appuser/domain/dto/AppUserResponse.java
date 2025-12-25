package com.masbytes.rbacapi.appuser.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

/**
 * Represents a user response object for API communication.
 * <p>
 * This record is used to transfer user-related data such as identifiers,
 * personal information, status, and timestamps. It ensures that null values
 * are excluded from JSON serialization and that date fields follow a
 * standardized UTC format.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppUserResponse(
        
    UUID publicId,
    String fullname,
    String email,
    String status,
    
    /** * Timestamp of when the user was created. 
     * Formatted as UTC in ISO-8601 with milliseconds. 
     */ 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    String createdAt,
    
    /**
     * Timestamp of the last update made to the user.
     * Formatted as UTC in ISO-8601 with milliseconds.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    String updatedAt

        
) {}