package com.masbytes.rbacapi.role.domain.entity;

import com.masbytes.rbacapi.shared.domain.auditable.BaseEntity;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a role in the authentication system. Maps to the "role"
 * table in the "auth" schema and includes name, description, and status fields
 * with validation and lifecycle hooks.
 */
@Entity
@Table(name = "role", schema = "auth")
@AttributeOverride(name = "id", column = @Column(name = "role_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(value = AccessLevel.PRIVATE)
@SuperBuilder
public class Role extends BaseEntity {

    @Column(name = "role_name", nullable = false, updatable = false, unique = true, length = 75)
    private String roleName;

    @Column(name = "role_description", nullable = false, length = 100)
    private String roleDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_status", nullable = false, length = 50)
    private Status roleStatus;

    /**
     * Initializes the role status before persisting. Defaults to Status.PENDING
     * if no status is set.
     */
    @PrePersist
    protected void initializeRoleStatus() {
        if (this.roleStatus == null) {
            this.roleStatus = Status.PENDING;
        }
    }

    /**
     * Updates the description of the role. Validates that the new description
     * is not null, not blank, and does not exceed 100 characters.
     *
     * @param newDescription the new description to set
     * @throws IllegalArgumentException if the description is invalid
     */
    public void updateRoleDescription(String newDescription) {
        if (newDescription == null || newDescription.isBlank()) {
            throw new IllegalArgumentException("Role description cannot be null or blank");
        }

        if (newDescription.length() > 100) {
            throw new IllegalArgumentException("Role description exceeds maximum length of 100 characters");
        }

        this.roleDescription = newDescription;
    }

    /**
     * Changes the status of the role. Validates that the new status is not null
     * and that the transition from the current status to the new one is
     * allowed.
     *
     * @param newStatus the new status to assign
     * @throws IllegalArgumentException if the status is null
     * @throws IllegalStateException if the transition is not permitted
     */
    public void changeRoleStatus(Status newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Role Status cannot be null");
        }

        if (!this.roleStatus.canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid transition from " + this.roleStatus + " to " + newStatus);
        }

        this.roleStatus = newStatus;
    }

}
