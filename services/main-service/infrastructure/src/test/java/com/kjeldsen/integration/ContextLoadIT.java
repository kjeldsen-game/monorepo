package com.kjeldsen.integration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextLoadIT extends AbstractIT {

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }
}