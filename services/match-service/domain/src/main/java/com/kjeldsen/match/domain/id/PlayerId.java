package com.kjeldsen.match.domain.id;

public final class PlayerId extends Id<PlayerId> {

    public PlayerId(String value) {
        super(value);
    }

    public static PlayerId generate() {
        return from(random());
    }

    private static PlayerId from(String value) {
        return createOrNull(value, PlayerId::new);
    }

}
