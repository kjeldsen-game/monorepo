package com.kjeldsen.auth.authentication.mappers;

import com.kjeldsen.auth.domain.Profile;
import com.kjeldsen.auth.rest.model.ProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileMapperTest {

    @Test
    @DisplayName("Should map profile to profile response")
    void should_map_profile_to_profile_response() {
        Profile profile = Profile.builder().email("email").teamName("teamName").avatar("avatar").build();
        ProfileResponse profileResponse = ProfileMapper.INSTANCE.map(profile);
        assertNotNull(profileResponse);
        assertEquals("email", profileResponse.getEmail());
        assertEquals("teamName", profileResponse.getTeamName());
        assertEquals("avatar", profileResponse.getAvatar());
    }
}