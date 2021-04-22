package com.gmail.kirilllapitsky.finnhub.security.enumerable;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    GUEST(Set.of(Permissions.VIEWING), 0),
    BEGINNER(Set.of(Permissions.TRACKING), 2),
    MIDDLE(Set.of(Permissions.TRACKING), 6),
    SENIOR(Set.of(Permissions.TRACKING), 10),
    ADMIN(Set.of(Permissions.ADMINISTRATE), 0);

    private final Set<SimpleGrantedAuthority> permissions;

    private final Integer maxNumberOfTrackingCompanies;

    Role(Set<Permissions> permissions, Integer maxNumberOfTrackingCompanies) {
        this.permissions = permissions
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        this.maxNumberOfTrackingCompanies = maxNumberOfTrackingCompanies;
    }

    public Set<SimpleGrantedAuthority> getPermissions() {
        return permissions;
    }

    public Integer getMaxNumberOfTrackingCompanies() {
        return maxNumberOfTrackingCompanies;
    }

}
