package com.kjeldsen.auth.domain.repositories;

import com.kjeldsen.auth.domain.User;

import java.util.Optional;

public interface UserReadRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(String username);
}
