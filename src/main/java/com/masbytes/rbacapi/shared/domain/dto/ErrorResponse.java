package com.masbytes.rbacapi.shared.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;

/**
 * Response DTO representing standardized error details for API responses.
 * Includes HTTP status, error code, message, request path, and timestamp.
 * Provides consistent structure for error handling across the application.
 */
public record ErrorResponse(
        
        /**
         * The HTTP status code of the error (e.g., 404, 400, 500).
         */
        int status, // Ejemplo: 404

        /**
         * The application-specific error code (e.g., "PERM-404").
         */
        String errorCode, // Ejemplo: "PERM-404" (Tu 'error')

        /**
         * A human-readable description of the error (e.g., "Permission does not
         * exist").
         */
        String message, // Ejemplo: "El permiso no existe"

        /**
         * The request path where the error occurred (e.g.,
         * "/api/v1/permissions/...").
         */
        String path, // Ejemplo: "/api/v1/permissions/..."

        /**
         * The timestamp when the error occurred. Formatted as ISO-8601 string
         * in UTC.
         */
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                timezone = "UTC"
        )
        Instant timestamp
        
        ) {

}
