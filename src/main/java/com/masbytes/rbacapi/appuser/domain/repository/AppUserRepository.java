package com.masbytes.rbacapi.appuser.domain.repository;

import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing AppUser entities.
 * <p>
 * Extends JpaRepository to provide CRUD operations and defines custom queries
 * for user lookup and validation.
 */
@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    /**
     * Finds a user by their public UUID.
     *
     * @param publicId the unique public identifier of the user
     * @return an Optional containing the user if found, or empty if not
     */
    Optional<AppUser> findByPublicId(UUID publicId);

    /**
     * Finds a user by their email address, typically used for authentication.
     *
     * @param email the email address of the user
     * @return an Optional containing the user if found, or empty if not
     */
    Optional<AppUser> findByEmail(String email);

    /**
     * Checks whether an email address is already registered in the system.
     *
     * @param email the email address to check
     * @return true if the email exists, false otherwise
     */
    boolean existsByEmail(String email);

}
