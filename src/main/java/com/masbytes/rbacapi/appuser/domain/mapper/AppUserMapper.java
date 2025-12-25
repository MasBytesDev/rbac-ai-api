package com.masbytes.rbacapi.appuser.domain.mapper;

import com.masbytes.rbacapi.appuser.domain.dto.AppUserResponse;
import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
import com.masbytes.rbacapi.appuser.domain.dto.CreateAppUserRequest;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset; // Importante
import java.time.format.DateTimeFormatter;

/**
 * Mapper component for converting between AppUser entities and DTOs.
 * <p>
 * Provides methods to transform request objects into entities and entities into
 * response objects, ensuring proper formatting of date fields in UTC ISO-8601
 * format.
 */
@Component
public class AppUserMapper {

    private static final DateTimeFormatter ISO_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .withZone(ZoneOffset.UTC); // Le decimos al formateador que use UTC

    /**
     * Converts a CreateAppUserRequest into an AppUser entity.
     *
     * @param request the request containing user details
     * @param encodedPassword the already encoded password to store
     * @return a new AppUser entity or null if the request is null
     */
    public AppUser toEntity(CreateAppUserRequest request, String encodedPassword) {

        if (request == null) {
            return null;
        }

        return AppUser.builder()
                .fullname(request.fullname())
                .email(request.email())
                .passwordHash(encodedPassword)
                .build();
    }

    /**
     * Converts an AppUser entity into an AppUserResponse DTO. Formats
     * timestamps in UTC ISO-8601 with milliseconds.
     *
     * @param user the AppUser entity to convert
     * @return a corresponding AppUserResponse or null if the user is null
     */
    public AppUserResponse toResponse(AppUser user) {

        if (user == null) {
            return null;
        }

        return new AppUserResponse(
                user.getPublicId(),
                user.getFullname(),
                user.getEmail(),
                user.getAppUserStatus().name(),
                // Usamos el formateador directamente, él se encarga de convertir el Instant
                user.getCreatedAt() != null ? ISO_FORMATTER.format(user.getCreatedAt()) : null,
                user.getUpdatedAt() != null ? ISO_FORMATTER.format(user.getUpdatedAt()) : null
        );
    }
}
