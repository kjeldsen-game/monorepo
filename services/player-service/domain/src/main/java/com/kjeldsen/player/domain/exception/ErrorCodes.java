package com.kjeldsen.player.domain.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodes {

    public static final String BLOOM_PLAYER_AGE_INVALID_RANGE = "Bloom years on must be between 0 and 10";
    public static final String BLOOM_YEARS_ON_INVALID_RANGE = "Bloom years on must be between 0 and 10";
    public static final String BLOOM_SPEED_INVALID_RANGE = "Bloom speed must be between 0-1000";

}
