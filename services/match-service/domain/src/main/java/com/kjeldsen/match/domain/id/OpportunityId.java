package com.kjeldsen.match.domain.id;

public final class OpportunityId extends Id<OpportunityId> {

    public OpportunityId(String value) {
        super(value);
    }

    public static OpportunityId generate() {
        return from(random());
    }

    private static OpportunityId from(String value) {
        return createOrNull(value, OpportunityId::new);
    }

}
