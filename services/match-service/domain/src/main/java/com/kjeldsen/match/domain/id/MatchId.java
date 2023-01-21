package com.kjeldsen.match.domain.id;

public final class MatchId extends Id<MatchId> {

    public MatchId(String value) {
        super(value);
    }

    public static MatchId generate() {
        return from(random());
    }

    private static MatchId from(String value) {
        return createOrNull(value, MatchId::new);
    }

}
