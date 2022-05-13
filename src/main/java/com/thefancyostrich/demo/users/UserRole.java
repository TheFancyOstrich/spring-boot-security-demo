package com.thefancyostrich.demo.users;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static com.thefancyostrich.demo.users.UserPermission.*;

public enum UserRole {
    USER(),
    MODERATOR(USER_READ),
    ADMIN(USER_MODIFY, USER_READ);

    private final Set<UserPermission> permissions;

    UserRole(UserPermission... permissions) {
        Set<UserPermission> permissionSet = new HashSet<>();
        for (UserPermission permission : permissions) {
            permissionSet.add(permission);
        }
        this.permissions = permissionSet;
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<GrantedAuthority> getGrantedAuthorities() {
        Set<GrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
