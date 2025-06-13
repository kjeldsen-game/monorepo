package com.kjeldsen.auth.persistence.adapters.mongo;

import com.kjeldsen.auth.domain.Role;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import com.kjeldsen.auth.persistence.common.AbstractMongoDbTest;
import com.kjeldsen.auth.persistence.mongo.repositories.UserMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Component.class))
@ActiveProfiles("test")
public class UserWriteRepositoryMongoAdapterIT extends AbstractMongoDbTest {

    @Autowired
    private UserMongoRepository userMongoRepository;
    @Autowired
    private UserWriteRepository userWriteRepository;

    @BeforeEach
    public void setup() {
        userMongoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Should save")
    class GetShouldByEmail {
        @Test
        @DisplayName("return user")
        void should_return_user() {
            User user = new User();
            user.setPassword("password");
            user.setRoles(Set.of(Role.USER));
            user.setEmail("email@email.com");
            userWriteRepository.save(user);
            Optional<User> repoResult = userMongoRepository.findByEmail("email@email.com");
            assertFalse(repoResult.isEmpty());
        }
    }
}
