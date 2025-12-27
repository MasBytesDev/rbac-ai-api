package com.masbytes.rbacapi.rolepermission.domain.entity;

import com.masbytes.rbacapi.permission.domain.entity.Permission;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.shared.domain.auditable.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing the association between a role and a permission. Maps to
 * the "role_permission" table in the "auth" schema and links roles with their
 * assigned permissions.
 */
@Entity
@Table(name = "role_permission", schema = "auth")
@AttributeOverride(name = "id", column = @Column(name = "role_permission_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(value = AccessLevel.PRIVATE)
@SuperBuilder
public class RolePermission extends BaseEntity {

    /**
     * The role associated with this permission assignment. Mapped as a
     * many-to-one relationship.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * The permission associated with this role assignment. Mapped as a
     * many-to-one relationship.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id", nullable = false)
    private Permission permission;

}
