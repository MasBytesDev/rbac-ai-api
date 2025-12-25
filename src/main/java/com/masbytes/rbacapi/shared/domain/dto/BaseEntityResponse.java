package com.masbytes.rbacapi.shared.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.UUID;
import java.time.Instant;

/**
 * Response DTO providing common metadata for entities. Includes the public
 * UUID, creation timestamp, and last update timestamp. Timestamps are formatted
 * in ISO-8601 with UTC timezone.
 */
public record BaseEntityResponse(
        
        /**
         * The public UUID of the entity.
         */
        UUID publicId,
        
        /**
         * The timestamp when the entity was created. Formatted as ISO-8601
         * string in UTC.
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant createdAt,
        
        /**
         * The timestamp when the entity was last updated. Formatted as ISO-8601
         * string in UTC.
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        Instant updatedAt
        
        ) {

}
