package com.kjeldsen.lib.annotations;

import java.lang.annotation.*;

/**
 * Indicates that the annotated element is intended for internal use only.
 */
@Target({ ElementType.METHOD, ElementType.TYPE }) // Can be applied to method or class
@Retention(RetentionPolicy.RUNTIME)                // Retained at runtime
@Documented
public @interface Internal {
}
