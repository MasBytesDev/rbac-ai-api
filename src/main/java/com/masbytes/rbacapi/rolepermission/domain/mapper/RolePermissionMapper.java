package com.masbytes.rbacapi.rolepermission.domain.mapper;

import com.masbytes.rbacapi.permission.domain.entity.Permission;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.rolepermission.domain.dto.PermissionSummaryResponse;
import com.masbytes.rbacapi.rolepermission.domain.dto.RolePermissionResponse;
import com.masbytes.rbacapi.rolepermission.domain.dto.RoleWithPermissionsResponse;
import com.masbytes.rbacapi.rolepermission.domain.entity.RolePermission;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper component for converting RolePermission entities and related objects
 * into their corresponding DTO responses. Provides methods for simple
 * association mapping, detailed role-permission mapping, and permission
 * summaries.
 */
@Component
public class RolePermissionMapper {

    /**
     * Converts a RolePermission entity into a RolePermissionResponse DTO.
     * Includes identifiers, names, and the timestamp of assignment.
     *
     * @param entity the RolePermission entity
     * @return the corresponding RolePermissionResponse DTO, or null if entity
     * is null
     */
    public RolePermissionResponse toRolePermissionResponse(RolePermission entity) {
        if (entity == null) {
            return null;
        }

        return new RolePermissionResponse(
                entity.getPublicId(),
                entity.getRole().getPublicId(),
                entity.getRole().getRoleName(),
                entity.getPermission().getPublicId(),
                entity.getPermission().getPermissionName(),
                entity.getCreatedAt().toString()
        );
    }

    /**
     * Converts a Role and its associated permissions into a
     * RoleWithPermissionsResponse DTO. Maps each permission into its summary
     * representation.
     *
     * @param role the Role entity
     * @param permissions the list of associated Permission entities
     * @return the corresponding RoleWithPermissionsResponse DTO, or null if
     * role is null
     */
    public RoleWithPermissionsResponse toRoleWithPermissionsResponse(Role role, List<Permission> permissions) {
        if (role == null) {
            return null;
        }

        List<PermissionSummaryResponse> permissionSummaries = permissions.stream()
                .map(this::toPermissionSummaryResponse)
                .collect(Collectors.toList());

        return new RoleWithPermissionsResponse(
                role.getPublicId(),
                role.getRoleName(),
                role.getRoleStatus().name(),
                permissionSummaries
        );
    }

    /**
     * Converts a Permission entity into a PermissionSummaryResponse DTO.
     *
     * @param permission the Permission entity
     * @return the corresponding PermissionSummaryResponse DTO, or null if
     * permission is null
     */
    public PermissionSummaryResponse toPermissionSummaryResponse(Permission permission) {
        if (permission == null) {
            return null;
        }

        return new PermissionSummaryResponse(
                permission.getPublicId(),
                permission.getPermissionName(),
                permission.getPermissionStatus().name()
        );
    }
}
