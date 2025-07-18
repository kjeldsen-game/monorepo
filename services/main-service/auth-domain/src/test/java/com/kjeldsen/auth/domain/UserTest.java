package com.kjeldsen.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Should convert avatar bytes to string")
    void should_convert_avatar_bytes_to_string() {
        User user = User.builder().build();
        byte[] avatarBytes = "test-image".getBytes();
        user.setAvatar(avatarBytes);
        String base64String = user.convertBytesToString();
        assertNotNull(base64String);
        assertEquals(Base64.getEncoder().encodeToString(avatarBytes), base64String);
    }

    @Test
    void shouldReturnNullIfAvatarIsNull() {
        User user = User.builder().build();
        user.setAvatar(null);
        String base64String = user.convertBytesToString();
        assertNull(base64String);
    }
}