package com.kjeldsen.match.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class IdTest {

    // Ensuring @EqualsAndHashCode is present in Id class
    @Test
    void equalsById() {
        String uuid = UUID.randomUUID().toString();
        Id id1 = new Id(uuid);
        Id id2 = new Id(uuid);
        assertEquals(id1, id2);
    }
}