package com.kjeldsen.auth.domain.providers;

import com.kjeldsen.auth.domain.models.Role;

import java.util.Set;

public interface JwtTokenProvider {

    String generateToken(String userId, String teamId,  Set<Role> roles);

    String generateInternalToken(String serviceName, String audience);
}
