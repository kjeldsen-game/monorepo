package com.kjeldsen.player.domain.provider;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class InstantProvider {

    public static Instant now() {
        return Instant.now();
    }

    public static Instant nowPlusDays(int days) {
        return Instant.now().plus(days, ChronoUnit.DAYS);
    }

    public static LocalDate nowAsLocalDate() {
        return LocalDate.now();
    }
}
