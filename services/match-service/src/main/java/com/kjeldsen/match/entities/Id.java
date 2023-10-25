package com.kjeldsen.match.entities;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@EqualsAndHashCode
@RequiredArgsConstructor
@Value
public class Id {

    String uuid;

    public static Id generate() {
        return new Id(UUID.randomUUID().toString());
    }
}
