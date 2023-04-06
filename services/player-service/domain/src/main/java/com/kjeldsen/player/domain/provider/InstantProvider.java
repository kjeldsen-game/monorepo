package com.kjeldsen.player.domain.provider;

import java.time.Instant;
import java.time.LocalDate;

public class InstantProvider {

    public static Instant now() {
        return Instant.now();
    }

    public static LocalDate nowAsLocalDate() {
        return LocalDate.now();
    }

}
