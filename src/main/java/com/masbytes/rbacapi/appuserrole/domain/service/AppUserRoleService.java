package com.masbytes.rbacapi.appuserrole.domain.service;

import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
import com.masbytes.rbacapi.appuser.domain.exception.AppUserNotFoundException;
import com.masbytes.rbacapi.appuser.domain.repository.AppUserRepository;
import com.masbytes.rbacapi.appuserrole.domain.mapper.AppUserRoleMapper;
import com.masbytes.rbacapi.appuserrole.domain.dto.AppUserRoleResponse;
import com.masbytes.rbacapi.appuserrole.domain.dto.AssignRoleRequest;
import com.masbytes.rbacapi.appuserrole.domain.dto.UpdateUserRoleRequest;
import com.masbytes.rbacapi.appuserrole.domain.entity.AppUserRole;
import com.masbytes.rbacapi.appuserrole.domain.exception.AppUserRoleAlreadyExistsException;
import com.masbytes.rbacapi.appuserrole.domain.exception.AppUserRoleNotFoundException;
import com.masbytes.rbacapi.appuserrole.domain.repository.AppUserRoleRepository;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.role.domain.exception.RoleNotFoundException;
import com.masbytes.rbacapi.role.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for managing user-role assignments. Provides business logic to
 * assign, update, and revoke roles for users, ensuring validation and
 * transactional consistency.
 */
@Service
@RequiredArgsConstructor
public class AppUserRoleService {

    private final AppUserRoleRepository appUserRoleRepository;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final AppUserRoleMapper mapper;

    /**
     * Assigns a new role to a user. Validates user and role existence, checks
     * for duplicates, and persists the new assignment.
     *
     * @param request the request containing user and role identifiers
     * @return the created role assignment as a response DTO
     * @throws AppUserNotFoundException if the user does not exist
     * @throws RoleNotFoundException if the role does not exist
     * @throws AppUserRoleAlreadyExistsException if the assignment already
     * exists
     */
    @Transactional
    public AppUserRoleResponse assignRole(AssignRoleRequest request) {
        // 1. Validar que el usuario existe
        AppUser user = appUserRepository.findByPublicId(request.userPublicId())
                .orElseThrow(() -> new AppUserNotFoundException(request.userPublicId()));

        // 2. Validar que el rol existe
        Role role = roleRepository.findByPublicId(request.rolePublicId())
                .orElseThrow(() -> new RoleNotFoundException(request.rolePublicId()));

        // 3. Validar que la relación no exista ya (Idempotencia)
        if (appUserRoleRepository.existsByAppUserPublicIdAndRolePublicId(user.getPublicId(), role.getPublicId())) {
            throw new AppUserRoleAlreadyExistsException(user.getPublicId(), role.getPublicId());
        }

        // 4. Crear y guardar
        AppUserRole newAssignment = mapper.toEntity(user, role);
        return mapper.toResponse(appUserRoleRepository.save(newAssignment));
    }

    /**
     * Updates a user's role assignment by replacing an old role with a new one.
     * Validates user existence, old role assignment, and new role availability.
     *
     * @param userPublicId the public UUID of the user
     * @param request the request containing old and new role identifiers
     * @return the updated role assignment as a response DTO
     * @throws AppUserNotFoundException if the user does not exist
     * @throws AppUserRoleNotFoundException if the old role is not assigned
     * @throws RoleNotFoundException if the new role does not exist
     * @throws AppUserRoleAlreadyExistsException if the new role is already
     * assigned
     */
    @Transactional
    public AppUserRoleResponse updateRole(java.util.UUID userPublicId, UpdateUserRoleRequest request) {
        // 1. Validar existencia del usuario
        AppUser user = appUserRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> new AppUserNotFoundException(userPublicId));

        // 2. Validar que el rol viejo esté asignado actualmente
        if (!appUserRoleRepository.existsByAppUserPublicIdAndRolePublicId(userPublicId, request.oldRolePublicId())) {
            throw new AppUserRoleNotFoundException(userPublicId, request.oldRolePublicId());
        }

        // 3. Validar que el rol nuevo exista
        Role newRole = roleRepository.findByPublicId(request.newRolePublicId())
                .orElseThrow(() -> new RoleNotFoundException(request.newRolePublicId()));

        // 4. Validar que el usuario no tenga YA el rol nuevo (para evitar duplicados tras el cambio)
        if (appUserRoleRepository.existsByAppUserPublicIdAndRolePublicId(userPublicId, request.newRolePublicId())) {
            throw new AppUserRoleAlreadyExistsException(userPublicId, request.newRolePublicId());
        }

        // 5. Ejecutar la sustitución: Eliminar el viejo, Crear el nuevo
        appUserRoleRepository.deleteByAppUserPublicIdAndRolePublicId(userPublicId, request.oldRolePublicId());

        AppUserRole newAssignment = mapper.toEntity(user, newRole);
        return mapper.toResponse(appUserRoleRepository.save(newAssignment));
    }

    /**
     * Revokes (deletes) a role assignment from a user. Validates that the
     * assignment exists before deletion.
     *
     * @param userPublicId the public UUID of the user
     * @param rolePublicId the public UUID of the role
     * @throws AppUserRoleNotFoundException if the assignment does not exist
     */
    @Transactional
    public void revokeRole(java.util.UUID userPublicId, java.util.UUID rolePublicId) {
        if (!appUserRoleRepository.existsByAppUserPublicIdAndRolePublicId(userPublicId, rolePublicId)) {
            throw new AppUserRoleNotFoundException(userPublicId, rolePublicId);
        }
        appUserRoleRepository.deleteByAppUserPublicIdAndRolePublicId(userPublicId, rolePublicId);
    }
}
