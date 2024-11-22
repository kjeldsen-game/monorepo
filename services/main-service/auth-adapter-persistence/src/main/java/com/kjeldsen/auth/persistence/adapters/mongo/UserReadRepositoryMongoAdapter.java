package com.kjeldsen.auth.persistence.adapters.mongo;

import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import com.kjeldsen.auth.persistence.mongo.repositories.UserMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReadRepositoryMongoAdapter implements UserReadRepository {

    private final UserMongoRepository userMongoRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userMongoRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        return userMongoRepository.findById(userId);
    }
}
