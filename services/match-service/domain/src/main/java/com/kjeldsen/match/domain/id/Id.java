package com.kjeldsen.match.domain.id;

import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@EqualsAndHashCode
public class Id<T extends Id<T>> {

    private final String uuid;

    protected Id(String value) {
        this.uuid = Objects.requireNonNull(value, "Id cannot be instantiated with 'null'");
    }

    protected static <T extends Id<T>> T createOrNull(CharSequence value, Function<String, T> constructor) {
        return value != null ? constructor.apply(value.toString()) : null;
    }

    protected static String random() {
        return UUID.randomUUID().toString();
    }

}
