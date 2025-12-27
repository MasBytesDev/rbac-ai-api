package com.masbytes.rbacapi.shared.infrastructure.security;

import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
//  import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
//  import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AppUserDetails implements UserDetails {

    private final AppUser user;

    public AppUserDetails(AppUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for (Role role : user.getRoles()) {
            // Rol
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));

            // Permisos del rol
            if (role.getRolePermissions() != null) {
                role.getRolePermissions().forEach(rp
                        -> authorities.add(new SimpleGrantedAuthority(rp.getPermission().getPermissionName()))
                );
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAppUserStatus() == Status.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAppUserStatus() == Status.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getAppUserStatus() == Status.ACTIVE;
    }

    @Override
    public boolean isEnabled() {
        return user.getAppUserStatus() == Status.ACTIVE;
    }
}
