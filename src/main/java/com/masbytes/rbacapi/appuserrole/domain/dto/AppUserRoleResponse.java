package com.masbytes.rbacapi.appuserrole.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

/**
 * Response DTO representing a role assignment for an application user. Includes
 * identifiers, user and role details, and assignment timestamp.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AppUserRoleResponse(
        UUID assignmentPublicId,
        UUID userPublicId,
        String userEmail,
        UUID rolePublicId,
        String roleName,
        
        /**
         * Timestamp of when the role was assigned. Formatted in UTC ISO-8601
         * with milliseconds precision.
         */
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        String assignedAt
        ) {

}
