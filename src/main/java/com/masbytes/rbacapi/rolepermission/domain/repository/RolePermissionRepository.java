package com.masbytes.rbacapi.rolepermission.domain.repository;

import com.masbytes.rbacapi.rolepermission.domain.entity.RolePermission;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing RolePermission entities. Extends
 * JpaRepository to provide CRUD operations and defines custom queries for
 * duplicate validation, revocation, and retrieval by role.
 */
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    /**
     * Checks if an association between a role and a permission already exists.
     *
     * @param rolePublicId the public UUID of the role
     * @param permissionPublicId the public UUID of the permission
     * @return true if the association exists, false otherwise
     */
    boolean existsByRolePublicIdAndPermissionPublicId(UUID rolePublicId, UUID permissionPublicId);

    /**
     * Finds the association between a role and a permission. Useful for
     * revoking a permission from a role.
     *
     * @param rolePublicId the public UUID of the role
     * @param permissionPublicId the public UUID of the permission
     * @return an Optional containing the RolePermission if found, or empty if
     * not
     */
    Optional<RolePermission> findByRolePublicIdAndPermissionPublicId(UUID rolePublicId, UUID permissionPublicId);

    /**
     * Retrieves all permissions associated with a given role.
     *
     * @param rolePublicId the public UUID of the role
     * @return a list of RolePermission entities linked to the role
     */
    List<RolePermission> findAllByRolePublicId(UUID rolePublicId);
}
