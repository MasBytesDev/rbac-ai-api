package com.masbytes.rbacapi.permission.domain.repository;

import com.masbytes.rbacapi.permission.domain.entity.Permission;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Permission entities. Extends JpaRepository
 * to provide CRUD operations and defines custom queries for lookup and
 * existence checks.
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * Finds a permission by its public UUID.
     *
     * @param publicId the public identifier of the permission
     * @return an Optional containing the permission if found, or empty if not
     */
    Optional<Permission> findByPublicId(UUID publicId);

    /**
     * Finds a permission by its unique name.
     *
     * @param permissionName the name of the permission
     * @return an Optional containing the permission if found, or empty if not
     */
    Optional<Permission> findByPermissionName(String permissionName);

    /**
     * Checks if a permission with the given name already exists.
     *
     * @param permissionName the name of the permission
     * @return true if a permission with the name exists, false otherwise
     */
    boolean existsByPermissionName(String permissionName);

}
