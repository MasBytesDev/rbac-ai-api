package com.masbytes.rbacapi.permission.domain.service;

import com.masbytes.rbacapi.permission.domain.dto.ChangePermissionStatusRequest;
import com.masbytes.rbacapi.permission.domain.dto.CreatePermissionRequest;
import com.masbytes.rbacapi.permission.domain.dto.PermissionResponse;
import com.masbytes.rbacapi.permission.domain.dto.UpdatePermissionDescriptionRequest;
import com.masbytes.rbacapi.permission.domain.entity.Permission;
import com.masbytes.rbacapi.permission.domain.exception.PermissionAlreadyExistsException;
import com.masbytes.rbacapi.permission.domain.exception.PermissionNotFoundException;
import com.masbytes.rbacapi.permission.domain.mapper.PermissionMapper;
import com.masbytes.rbacapi.permission.domain.repository.PermissionRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for managing Permission entities. Provides business logic for
 * creation, retrieval, updates, and status changes, ensuring validation and
 * transactional consistency.
 */
@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository repository;
    private final PermissionMapper mapper;

    /**
     * Creates a new permission. Validates uniqueness by name, maps the request
     * to an entity, persists it, and returns a response DTO.
     *
     * @param request the creation request containing permission details
     * @return the created permission as a response DTO
     * @throws PermissionAlreadyExistsException if a permission with the same
     * name already exists
     */
    @Transactional
    public PermissionResponse create(CreatePermissionRequest request) {
        if (repository.existsByPermissionName(request.permissionName())) {
            throw new PermissionAlreadyExistsException(request.permissionName());
        }

        // Mapper: De Request a Entidad
        Permission permission = mapper.toEntity(request);

        // Persistencia y retorno mapeado a Response
        return mapper.toResponse(repository.save(permission));
    }

    /**
     * Retrieves a permission by its public UUID.
     *
     * @param publicId the public identifier of the permission
     * @return the permission as a response DTO
     * @throws PermissionNotFoundException if no permission is found with the
     * given ID
     */
    @Transactional(readOnly = true)
    public PermissionResponse getByPublicId(UUID publicId) {
        return repository.findByPublicId(publicId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new PermissionNotFoundException(publicId));
    }

    /**
     * Updates the description of an existing permission. Uses the mapper to
     * apply the change and validates internally.
     *
     * @param publicId the public identifier of the permission
     * @param request the request containing the new description
     * @return the updated permission as a response DTO
     * @throws PermissionNotFoundException if the permission does not exist
     */
    @Transactional
    public PermissionResponse updateDescription(UUID publicId, UpdatePermissionDescriptionRequest request) {
        Permission permission = findEntityByPublicId(publicId);

        // El mapper aplica el cambio y la entidad lo valida internamente
        mapper.updateEntityFromDescriptionRequest(request, permission);

        return mapper.toResponse(repository.save(permission));
    }

    /**
     * Changes the status of an existing permission. Uses the mapper to apply
     * the new status, validating transitions.
     *
     * @param publicId the public identifier of the permission
     * @param request the request containing the new status
     * @return the updated permission as a response DTO
     * @throws PermissionNotFoundException if the permission does not exist
     */
    @Transactional
    public PermissionResponse changeStatus(UUID publicId, ChangePermissionStatusRequest request) {
        Permission permission = findEntityByPublicId(publicId);

        // El mapper le dice a la entidad que cambie su estado (y canTransitionTo se ejecuta)
        mapper.updateEntityFromStatusRequest(request, permission);

        return mapper.toResponse(repository.save(permission));
    }

    /**
     * Helper method to retrieve a Permission entity by its public UUID.
     *
     * @param publicId the public identifier of the permission
     * @return the Permission entity
     * @throws PermissionNotFoundException if no permission is found
     */
    private Permission findEntityByPublicId(UUID publicId) {
        return repository.findByPublicId(publicId)
                .orElseThrow(() -> new PermissionNotFoundException(publicId));
    }
}
