package com.masbytes.rbacapi.appuserrole.domain.repository;

import com.masbytes.rbacapi.appuserrole.domain.entity.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing AppUserRole entities. Extends JpaRepository
 * to provide CRUD operations and defines custom queries for role assignment
 * validation and lookup.
 */
@Repository
public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Long> {

    /**
     * Checks if a user already has a specific role assigned.
     *
     * @param userPublicId the public UUID of the user
     * @param rolePublicId the public UUID of the role
     * @return true if the assignment exists, false otherwise
     */
    boolean existsByAppUserPublicIdAndRolePublicId(UUID userPublicId, UUID rolePublicId);

    /**
     * Finds a role assignment by its public UUID.
     *
     * @param publicId the public identifier of the assignment
     * @return an Optional containing the assignment if found, or empty if not
     */
    Optional<AppUserRole> findByPublicId(UUID publicId);

    /**
     * Deletes a role assignment for a given user and role.
     *
     * @param userPublicId the public UUID of the user
     * @param rolePublicId the public UUID of the role
     */
    void deleteByAppUserPublicIdAndRolePublicId(UUID userPublicId, UUID rolePublicId);
}
