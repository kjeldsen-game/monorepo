package com.kjeldsen.auth.persistence.adapters.mongo;

import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import com.kjeldsen.auth.persistence.mongo.repositories.UserMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserWriteRepositoryMongoAdapter implements UserWriteRepository {

    private final UserMongoRepository userMongoRepository;

    @Override
    public User save(User user) {
        return  userMongoRepository.save(user);
    }
}

