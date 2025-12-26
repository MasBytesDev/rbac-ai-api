package com.masbytes.rbacapi.shared.infrastructure.security;

import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AppUserDetails implements UserDetails {

    private final AppUser user;

    public AppUserDetails(AppUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por ahora devolvemos vacío; se llenará en el UserDetailsService
        return Collections.emptyList();
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
