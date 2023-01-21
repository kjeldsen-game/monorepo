package com.kjeldsen.match.domain.id;

public final class PlayId extends Id<PlayId> {

    public PlayId(String value) {
        super(value);
    }

    public static PlayId generate() {
        return from(random());
    }

    private static PlayId from(String value) {
        return createOrNull(value, PlayId::new);
    }

}
