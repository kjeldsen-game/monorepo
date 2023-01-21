package com.kjeldsen.match.domain.id;

public final class DuelId extends Id<DuelId> {

    public DuelId(String value) {
        super(value);
    }

    public static DuelId generate() {
        return from(random());
    }

    private static DuelId from(String value) {
        return createOrNull(value, DuelId::new);
    }

}
