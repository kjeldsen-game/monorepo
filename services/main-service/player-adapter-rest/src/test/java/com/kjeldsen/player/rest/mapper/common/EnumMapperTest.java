package com.kjeldsen.player.rest.mapper.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class EnumMapperTest {

    private enum TestSourceEnum {
        FOO, BAR
    }

    private enum TestTargetEnum {
        FOO, BAR
    }

    private enum TestDifferentEnum {
        OTHER
    }

    @ParameterizedTest
    @EnumSource(TestSourceEnum.class)
    @DisplayName("Should map correctly")
    void should_map_correctly(TestSourceEnum testSourceEnum) {
        TestTargetEnum testTargetEnum = EnumMapper.mapEnum(testSourceEnum, TestTargetEnum.class);
        assertNotNull(testTargetEnum);
        assertEquals(testSourceEnum.name(), testTargetEnum.name());
    }

    @Test
    @DisplayName("Should return null when source is null")
    void should_return_null_when_source_is_null() {
        assertNull(EnumMapper.mapEnum(null, TestSourceEnum.class));
    }

    @Test
    void mapEnum_shouldThrowException_whenTargetDoesNotContainName() {
        assertThrows(IllegalArgumentException.class,
            () -> EnumMapper.mapEnum(TestSourceEnum.FOO, TestDifferentEnum.class));
    }
}