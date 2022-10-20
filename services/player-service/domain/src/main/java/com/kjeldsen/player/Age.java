package com.kjeldsen.player;

import org.apache.commons.lang3.Range;

record Age(Integer value){

    private static final Integer MIN_AGE = 15;
    private static final Integer MAX_AGE = 33;
    private static final Range<Integer> RANGE_OF_AGE = Range.between(MIN_AGE, MAX_AGE);

    public static Age of(int age) {
        return RANGE_OF_AGE.contains(age) ? new Age(age) : null;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
