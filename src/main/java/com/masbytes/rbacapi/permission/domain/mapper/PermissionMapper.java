package com.masbytes.rbacapi.permission.domain.mapper;

import com.masbytes.rbacapi.permission.domain.dto.*;
import com.masbytes.rbacapi.permission.domain.entity.Permission;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper component for converting between Permission entities and DTOs.
 * Provides methods to transform creation requests into entities, entities into
 * response DTOs, and to apply updates from request objects.
 */
@Component
public class PermissionMapper {

    /**
     * Converts a CreatePermissionRequest into a new Permission entity.
     *
     * @param request the creation request containing name and description
     * @return a new Permission entity
     * @throws NullPointerException if the request is null
     */
    public Permission toEntity(CreatePermissionRequest request) {
        Objects.requireNonNull(request, "Mapping failed: CreatePermissionRequest is null");

        return Permission.builder()
                .permissionName(request.permissionName())
                .permissionDescription(request.permissionDescription())
                .build();
    }

    /**
     * Converts a Permission entity into a response DTO.
     *
     * @param entity the Permission entity
     * @return the corresponding PermissionResponse
     * @throws NullPointerException if the entity is null
     */
    public PermissionResponse toResponse(Permission entity) {
        Objects.requireNonNull(entity, "Mapping failed: Permission entity is null");

        return new PermissionResponse(
                entity.getPublicId(),
                entity.getPermissionName(),
                entity.getPermissionDescription(),
                entity.getPermissionStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    /**
     * Updates the description of an existing Permission entity using data from
     * an UpdatePermissionDescriptionRequest.
     *
     * @param request the request containing the new description
     * @param entity the target Permission entity to update
     * @throws NullPointerException if request or entity is null
     */
    public void updateEntityFromDescriptionRequest(UpdatePermissionDescriptionRequest request, Permission entity) {
        Objects.requireNonNull(request, "Mapping failed: UpdatePermissionDescriptionRequest is null");
        Objects.requireNonNull(entity, "Mapping failed: Target Permission entity is null");

        entity.updatePermissionDescription(request.newDescription());
    }

    /**
     * Updates the status of an existing Permission entity using data from a
     * ChangePermissionStatusRequest.
     *
     * @param request the request containing the new status
     * @param entity the target Permission entity to update
     * @throws NullPointerException if request or entity is null
     */
    public void updateEntityFromStatusRequest(ChangePermissionStatusRequest request, Permission entity) {
        Objects.requireNonNull(request, "Mapping failed: ChangePermissionStatusRequest is null");
        Objects.requireNonNull(entity, "Mapping failed: Target Permission entity is null");

        entity.changePermissionStatus(request.newStatus());
    }
}
