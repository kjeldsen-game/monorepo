package com.kjeldsen.match.domain.id;

public final class ModifierId extends Id<ModifierId> {

    public ModifierId(String value) {
        super(value);
    }

    public static ModifierId generate() {
        return from(random());
    }

    private static ModifierId from(String value) {
        return createOrNull(value, ModifierId::new);
    }

}
