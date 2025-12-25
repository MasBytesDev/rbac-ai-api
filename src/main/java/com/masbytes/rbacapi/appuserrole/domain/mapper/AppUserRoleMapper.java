package com.masbytes.rbacapi.appuserrole.domain.mapper;

import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
import com.masbytes.rbacapi.appuserrole.domain.dto.AppUserRoleResponse;
import com.masbytes.rbacapi.appuserrole.domain.entity.AppUserRole;
import com.masbytes.rbacapi.role.domain.entity.Role;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Mapper component for converting between AppUserRole entities and DTOs.
 * Provides methods to transform domain objects into relationship entities and
 * entities into response DTOs with proper timestamp formatting.
 */
@Component
public class AppUserRoleMapper {

    private static final DateTimeFormatter ISO_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    /**
     * Converts domain objects into an AppUserRole entity. Used in the service
     * layer after validating user and role existence.
     *
     * @param user the application user
     * @param role the role to assign
     * @return a new AppUserRole entity or null if inputs are null
     */
    public AppUserRole toEntity(AppUser user, Role role) {
        if (user == null || role == null) {
            return null;
        }

        return AppUserRole.builder()
                .appUser(user)
                .role(role)
                .build();
    }

    /**
     * Converts an AppUserRole entity into a response DTO. Formats the
     * assignment timestamp in UTC ISO-8601 with milliseconds.
     *
     * @param entity the AppUserRole entity
     * @return the corresponding AppUserRoleResponse or null if entity is null
     */
    public AppUserRoleResponse toResponse(AppUserRole entity) {
        if (entity == null) {
            return null;
        }

        return new AppUserRoleResponse(
                entity.getPublicId(),
                entity.getAppUser().getPublicId(),
                entity.getAppUser().getEmail(),
                entity.getRole().getPublicId(),
                entity.getRole().getRoleName(),
                ISO_FORMATTER.format(entity.getCreatedAt())
        );
    }
}
