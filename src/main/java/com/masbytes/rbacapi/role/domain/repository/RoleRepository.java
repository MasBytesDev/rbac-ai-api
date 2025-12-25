package com.masbytes.rbacapi.role.domain.repository;

import com.masbytes.rbacapi.role.domain.entity.Role;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Role entities. Extends JpaRepository to
 * provide CRUD operations and defines custom queries for lookup and existence
 * checks.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its public UUID.
     *
     * @param publicId the public identifier of the role
     * @return an Optional containing the role if found, or empty if not
     */
    Optional<Role> findByPublicId(UUID publicId);

    /**
     * Finds a role by its unique name.
     *
     * @param roleName the name of the role
     * @return an Optional containing the role if found, or empty if not
     */
    Optional<Role> findByRoleName(String roleName);

    /**
     * Checks if a role with the given name already exists.
     *
     * @param roleName the name of the role
     * @return true if a role with the name exists, false otherwise
     */
    boolean existsByRoleName(String roleName);

}
