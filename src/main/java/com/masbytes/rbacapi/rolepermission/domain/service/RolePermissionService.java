package com.masbytes.rbacapi.rolepermission.domain.service;

import com.masbytes.rbacapi.permission.domain.entity.Permission;
import com.masbytes.rbacapi.permission.domain.repository.PermissionRepository;
import com.masbytes.rbacapi.permission.domain.exception.PermissionNotFoundException;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.role.domain.repository.RoleRepository;
import com.masbytes.rbacapi.role.domain.exception.RoleNotFoundException;
import com.masbytes.rbacapi.rolepermission.domain.mapper.RolePermissionMapper;
import com.masbytes.rbacapi.rolepermission.domain.dto.AssignPermissionRequest;
import com.masbytes.rbacapi.rolepermission.domain.dto.RolePermissionResponse;
import com.masbytes.rbacapi.rolepermission.domain.dto.RoleWithPermissionsResponse;
import com.masbytes.rbacapi.rolepermission.domain.entity.RolePermission;
import com.masbytes.rbacapi.rolepermission.domain.exception.PermissionAlreadyAssignedException;
import com.masbytes.rbacapi.rolepermission.domain.exception.RolePermissionNotFoundException;
import com.masbytes.rbacapi.rolepermission.domain.repository.RolePermissionRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for managing associations between roles and permissions.
 * Provides business logic for assigning, revoking, and retrieving
 * role-permission relationships, ensuring transactional consistency and
 * validation.
 */
@Service
@RequiredArgsConstructor
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionMapper mapper;

    /**
     * Assigns a permission to a role. Validates that both role and permission
     * exist, checks for duplicates, creates the association, persists it, and
     * returns a response DTO.
     *
     * @param request the request containing role and permission identifiers
     * @return the created role-permission association as a response DTO
     * @throws RoleNotFoundException if the role does not exist
     * @throws PermissionNotFoundException if the permission does not exist
     * @throws PermissionAlreadyAssignedException if the permission is already
     * assigned to the role
     */
    @Transactional
    public RolePermissionResponse assignPermissionToRole(AssignPermissionRequest request) {
        // 1. Validar si el Rol existe
        Role role = roleRepository.findByPublicId(request.rolePublicId())
                .orElseThrow(() -> new RoleNotFoundException(request.rolePublicId()));

        // 2. Validar si el Permiso existe
        Permission permission = permissionRepository.findByPublicId(request.permissionPublicId())
                .orElseThrow(() -> new PermissionNotFoundException(request.permissionPublicId()));

        // 3. Validar si ya existe la relación para evitar duplicados
        if (rolePermissionRepository.existsByRolePublicIdAndPermissionPublicId(
                request.rolePublicId(), request.permissionPublicId())) {
            throw new PermissionAlreadyAssignedException(role.getRoleName(), permission.getPermissionName());
        }

        // 4. Crear la entidad de asociación
        RolePermission rolePermission = RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();

        // 5. Guardar y mapear a respuesta
        RolePermission saved = rolePermissionRepository.save(rolePermission);
        return mapper.toRolePermissionResponse(saved);
    }

    /**
     * Revokes a permission from a role by deleting the association.
     *
     * @param rolePublicId the public UUID of the role
     * @param permissionPublicId the public UUID of the permission
     * @throws RolePermissionNotFoundException if the association does not exist
     */
    @Transactional
    public void revokePermissionFromRole(UUID rolePublicId, UUID permissionPublicId) {
        // 1. Buscamos la asociación específica
        RolePermission association = rolePermissionRepository
                .findByRolePublicIdAndPermissionPublicId(rolePublicId, permissionPublicId)
                .orElseThrow(() -> new RolePermissionNotFoundException(rolePublicId, permissionPublicId));

        // 2. La eliminamos de la base de datos
        rolePermissionRepository.delete(association);
    }

    /**
     * Retrieves a role along with all its associated permissions. Validates
     * that the role exists, fetches associations, extracts permissions, and
     * maps them into a detailed response DTO.
     *
     * @param rolePublicId the public UUID of the role
     * @return the role with its associated permissions as a response DTO
     * @throws RoleNotFoundException if the role does not exist
     */
    @Transactional(readOnly = true)
    public RoleWithPermissionsResponse getRoleWithPermissions(UUID rolePublicId) {
        // 1. Validar que el Rol existe
        Role role = roleRepository.findByPublicId(rolePublicId)
                .orElseThrow(() -> new RoleNotFoundException(rolePublicId));

        // 2. Obtener la lista de asociaciones
        List<RolePermission> associations = rolePermissionRepository.findAllByRolePublicId(rolePublicId);

        // 3. Extraer solo los permisos de las asociaciones
        List<Permission> permissions = associations.stream()
                .map(RolePermission::getPermission)
                .toList();

        // 4. Mapear a la respuesta enriquecida (Role + Lista de Permisos)
        return mapper.toRoleWithPermissionsResponse(role, permissions);
    }
}
