package com.masbytes.rbacapi.role.domain.mapper;

import com.masbytes.rbacapi.role.domain.dto.CreateRoleRequest;
import com.masbytes.rbacapi.role.domain.dto.RoleResponse;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import org.springframework.stereotype.Component;

import java.util.Collections;
// import java.util.Set;
// import java.util.stream.Collectors;

/**
 * Mapper component for converting between Role entities and DTOs. Provides
 * methods to transform creation requests into entities and entities into
 * response DTOs.
 */
@Component
public class RoleMapper {

    /**
     * Converts a CreateRoleRequest into a new Role entity. Status is set to
     * PENDING by default, although it can also be initialized via @PrePersist
     * in the entity.
     *
     * @param request the creation request containing role details
     * @return a new Role entity
     */
    public Role toEntity(CreateRoleRequest request) {
        return Role.builder()
                .roleName(request.roleName())
                .roleDescription(request.roleDescription())
                .roleStatus(Status.PENDING)
                .build();
    }

    /**
     * Converts a Role entity into a RoleResponse DTO. Currently returns an
     * empty set of permissions until RolePermission mapping is implemented.
     *
     * @param role the Role entity
     * @return the corresponding RoleResponse DTO
     */
    public RoleResponse toResponse(Role role) {
        return new RoleResponse(
                role.getPublicId(),
                role.getRoleName(),
                role.getRoleDescription(),
                role.getRoleStatus(),
                Collections.emptySet(),
                role.getCreatedAt(),
                role.getUpdatedAt()
        );
    }
}
