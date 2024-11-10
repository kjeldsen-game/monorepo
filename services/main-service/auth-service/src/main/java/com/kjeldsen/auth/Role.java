package com.kjeldsen.auth;

import java.util.Set;

public enum Role {

    USER,
    ADMIN;

    public static Set<String> getPermissions(Role role) {
        return switch (role) {
            case USER:
                yield Set.of("team:read", "team:write");
            case ADMIN:
                yield Set.of("team:read", "team:write", "team:delete");
        };
    }
}
