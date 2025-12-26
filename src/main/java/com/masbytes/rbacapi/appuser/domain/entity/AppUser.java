package com.masbytes.rbacapi.appuser.domain.entity;

import com.masbytes.rbacapi.appuserrole.domain.entity.AppUserRole;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.shared.domain.auditable.BaseEntity;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing an application user within the authentication schema.
 * <p>
 * Maps to the "app_user" table and stores user details such as fullname, email,
 * password hash, and status. Includes lifecycle hooks and domain methods to
 * safely update user information while enforcing validation and state
 * transition rules.
 */
@Entity
@Table(name = "app_user", schema = "auth")
@AttributeOverride(name = "id", column = @Column(name = "app_user_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(value = AccessLevel.PRIVATE)
@SuperBuilder
public class AppUser extends BaseEntity {

    @Column(name = "fullname", nullable = false, length = 100)
    private String fullname;

    @Column(name = "email", nullable = false, updatable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_user_status", nullable = false, length = 50)
    private Status appUserStatus;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AppUserRole> appUserRoles;

    /**
     * Lifecycle hook executed before persisting the entity. Ensures that the
     * user status is initialized to PENDING if not set.
     */
    @PrePersist
    protected void initializeAppUserStatus() {

        if (this.appUserStatus == null) {
            this.appUserStatus = Status.PENDING;
        }
    }

    /**
     * Updates the user's fullname with validation.
     *
     * @param newFullname the new fullname to assign
     * @throws IllegalArgumentException if the fullname is null, blank, or
     * exceeds 100 characters
     */
    public void updateFullname(String newFullname) {

        if (newFullname == null || newFullname.isBlank()) {
            throw new IllegalArgumentException("Fullname cannot be null or blank");
        }

        if (newFullname.length() > 100) {
            throw new IllegalArgumentException("Fullname exceeds maximum length of 100 characters long");
        }

        this.fullname = newFullname;
    }

    /**
     * Changes the user's password hash with validation.
     *
     * @param newPasswordHash the new password hash to assign
     * @throws IllegalArgumentException if the password hash is null, blank, or
     * exceeds 255 characters
     */
    public void changePasswordHash(String newPasswordHash) {

        if (newPasswordHash == null || newPasswordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be null or blank");
        }

        if (newPasswordHash.length() > 255) {
            throw new IllegalArgumentException("Password hash exceeds maximum length of 255 characters");
        }

        this.passwordHash = newPasswordHash;
    }

    /**
     * Changes the user's status with validation.
     *
     * @param newStatus the new status to assign
     * @throws IllegalArgumentException if the status is null
     * @throws IllegalStateException if the transition is not allowed from the
     * current status
     */
    public void changeAppUserStatus(Status newStatus) {

        if (newStatus == null) {
            throw new IllegalArgumentException("AppUser Status cannot be null");
        }

        if (!this.appUserStatus.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid transition from " + this.appUserStatus + " to " + newStatus);
        }

        this.appUserStatus = newStatus;
    }

    /**
     * Convenience method to retrieve the roles assigned to the user.
     *
     * @return a set of Role entities linked to this user *
     */
    public Set<Role> getRoles() {
        if (appUserRoles == null || appUserRoles.isEmpty()) {
            return Collections.emptySet();
        }
        return appUserRoles.stream()
                .map(AppUserRole::getRole)
                .collect(Collectors.toSet());
    }

}
