package com.masbytes.rbacapi.permission.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import java.util.UUID;
import java.time.Instant;

/**
 * Response DTO representing a permission entity. Includes identifiers, name,
 * description, status, and audit timestamps.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PermissionResponse(
        
        UUID publicId,
        String permissionName,
        String permissionDescription,
        Status permissionStatus,
        
        /**
         * Timestamp when the permission was created. Formatted in UTC ISO-8601
         * with milliseconds precision.
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant createdAt,
        
        /**
         * Timestamp when the permission was last updated. Formatted in UTC
         * ISO-8601 with milliseconds precision.
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant updatedAt
        
        ) {

}
