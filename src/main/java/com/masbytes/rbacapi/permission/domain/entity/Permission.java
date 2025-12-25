package com.masbytes.rbacapi.permission.domain.entity;

import com.masbytes.rbacapi.permission.domain.exception.InvalidPermissionFormatException;
import com.masbytes.rbacapi.permission.domain.exception.InvalidPermissionStatusTransitionException;
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
 * Entity representing a permission in the authentication system. Maps to the
 * "permission" table in the "auth" schema and includes name, description, and
 * status fields with validation and lifecycle hooks.
 */
@Entity
@Table(name = "permission", schema = "auth")
@AttributeOverride(name = "id", column = @Column(name = "permission_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(value = AccessLevel.PRIVATE)
@SuperBuilder
public class Permission extends BaseEntity {

    @Column(name = "permission_name", nullable = false, updatable = false, unique = true, length = 75)
    private String permissionName;

    @Column(name = "permission_description", nullable = false, length = 100)
    private String permissionDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_status", nullable = false, length = 50)
    private Status permissionStatus;

    /**
     * Initializes the permission status before persisting. Defaults to
     * Status.PENDING if no status is set.
     */
    @PrePersist
    protected void initializePermissionStatus() {
        if (this.permissionStatus == null) {
            this.permissionStatus = Status.PENDING;
        }
    }

    /**
     * Updates the description of the permission. Validates that the new
     * description is not null, not blank, and does not exceed 100 characters.
     *
     * @param newDescription the new description to set
     * @throws IllegalArgumentException if the description is invalid
     */
    public void updatePermissionDescription(String newDescription) {
        if (newDescription == null || newDescription.isBlank()) {
            throw new IllegalArgumentException("Permission description cannot be null or blank");
        }

        if (newDescription.length() > 100) {
            throw new IllegalArgumentException("Permission description exceeds maximum length of 100 characters");
        }

        this.permissionDescription = newDescription;
    }

    /**
     * Changes the status of the permission. Validates that the new status is
     * not null and that the transition from the current status to the new one
     * is allowed.
     *
     * @param newStatus the new status to assign
     * @throws InvalidPermissionFormatException if the status is null
     * @throws InvalidPermissionStatusTransitionException if the transition is
     * not permitted
     */
    public void changePermissionStatus(Status newStatus) {
        if (newStatus == null) {
            throw new InvalidPermissionFormatException("Status cannot be null");
        }

        if (!this.permissionStatus.canTransitionTo(newStatus)) {
            throw new InvalidPermissionStatusTransitionException(this.permissionStatus, newStatus);
        }

        this.permissionStatus = newStatus;
    }

}
