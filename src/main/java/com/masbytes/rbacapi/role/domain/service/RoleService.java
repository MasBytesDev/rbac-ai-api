package com.masbytes.rbacapi.role.domain.service;

import com.masbytes.rbacapi.role.domain.mapper.RoleMapper;
import com.masbytes.rbacapi.role.domain.dto.*;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.role.domain.exception.RoleAlreadyExistsException;
import com.masbytes.rbacapi.role.domain.exception.RoleNotFoundException;
import com.masbytes.rbacapi.role.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service layer for managing Role entities. Provides business logic for
 * creation, retrieval, description updates, and status changes, ensuring
 * transactional consistency and validation.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    /**
     * Creates a new role. Validates uniqueness by name, maps the request to an
     * entity, persists it, and returns a response DTO.
     *
     * @param request the creation request containing role details
     * @return the created role as a response DTO
     * @throws RoleAlreadyExistsException if a role with the same name already
     * exists
     */
    @Transactional
    public RoleResponse createRole(CreateRoleRequest request) {
        if (roleRepository.existsByRoleName(request.roleName())) {
            throw new RoleAlreadyExistsException(request.roleName());
        }

        Role role = roleMapper.toEntity(request);
        return roleMapper.toResponse(roleRepository.save(role));
    }

    /**
     * Retrieves a role by its public UUID.
     *
     * @param publicId the public identifier of the role
     * @return the role as a response DTO
     * @throws RoleNotFoundException if no role is found with the given ID
     */
    public RoleResponse getRoleByPublicId(UUID publicId) {
        return roleRepository.findByPublicId(publicId)
                .map(roleMapper::toResponse)
                .orElseThrow(() -> new RoleNotFoundException(publicId.toString()));
    }

    /**
     * Updates the description of an existing role. Validates the new
     * description before applying changes.
     *
     * @param publicId the public identifier of the role
     * @param request the request containing the new description
     * @return the updated role as a response DTO
     * @throws RoleNotFoundException if the role does not exist
     * @throws IllegalArgumentException if the description is invalid
     */
    @Transactional
    public RoleResponse updateDescription(UUID publicId, UpdateRoleDescriptionRequest request) {
        Role role = roleRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RoleNotFoundException(publicId.toString()));

        role.updateRoleDescription(request.newDescription());
        return roleMapper.toResponse(roleRepository.save(role));
    }

    /**
     * Changes the status of an existing role. Validates the transition before
     * applying changes.
     *
     * @param publicId the public identifier of the role
     * @param request the request containing the new status
     * @return the updated role as a response DTO
     * @throws RoleNotFoundException if the role does not exist
     */
    @Transactional
    public RoleResponse changeStatus(UUID publicId, ChangeRoleStatusRequest request) {
        Role role = roleRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RoleNotFoundException(publicId.toString()));

        role.changeRoleStatus(request.newStatus());
        return roleMapper.toResponse(roleRepository.save(role));
    }
}
