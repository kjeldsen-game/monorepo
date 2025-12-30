package com.kjeldsen.auth.domain.providers;

import com.kjeldsen.auth.domain.Role;

import java.util.Set;

public interface JwtTokenProvider {

    String generateToken(String userId, String teamId,  Set<Role> roles);
}
