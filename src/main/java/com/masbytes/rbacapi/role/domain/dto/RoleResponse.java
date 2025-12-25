package com.masbytes.rbacapi.role.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/**
 * Response DTO representing a role entity. Includes identifiers, name,
 * description, status, associated permissions, and audit timestamps.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoleResponse(
        
        UUID publicId,
        String roleName,
        String roleDescription,
        Status roleStatus,
        
        /**
         * The set of permission names associated with the role.
         */
        Set<String> permissions, // Nombres de los permisos asociados

        /**
         * Timestamp when the role was created. Formatted in UTC ISO-8601 with
         * milliseconds precision.
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant createdAt,
        
        /**
         * Timestamp when the role was last updated. Formatted in UTC ISO-8601
         * with milliseconds precision.
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant updatedAt
        ) {

}
