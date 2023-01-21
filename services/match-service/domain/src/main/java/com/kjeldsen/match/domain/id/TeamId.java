package com.kjeldsen.match.domain.id;

public final class TeamId extends Id<TeamId> {

    public TeamId(String value) {
        super(value);
    }

    public static TeamId generate() {
        return from(random());
    }

    private static TeamId from(String value) {
        return createOrNull(value, TeamId::new);
    }

}
