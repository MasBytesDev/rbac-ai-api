package com.masbytes.rbacapi.appuserrole.domain.entity;

import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
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
 * Entity representing the assignment of a role to an application user. Maps to
 * the "app_user_role" table in the "auth" schema.
 */
@Entity
@Table(name = "app_user_role", schema = "auth")
@AttributeOverride(name = "id", column = @Column(name = "app_user_role_id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter(value = AccessLevel.PRIVATE)
@SuperBuilder
public class AppUserRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id", nullable = false)
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * Factory method to create a new AppUserRole association.
     *
     * @param user the application user to assign
     * @param role the role to assign
     * @return a new AppUserRole instance linking the user and role
     * @throws IllegalArgumentException if user or role is null
     */
    public static AppUserRole create(AppUser user, Role role) {
        if (user == null || role == null) {
            throw new IllegalArgumentException("AppUser and Role must not be null");
        }
        return AppUserRole.builder()
                .appUser(user)
                .role(role)
                .build();
    }

}
