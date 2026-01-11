package com.kjeldsen.auth.persistence.adapters.mongo;

import com.kjeldsen.auth.domain.models.Role;
import com.kjeldsen.auth.domain.models.User;
import com.kjeldsen.auth.domain.repositories.UserReadRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Component.class))
@ActiveProfiles("test")
public class UserReadMongoRepositoryAdapterIT extends AbstractMongoDbTest {

    @Autowired
    private UserMongoRepository userMongoRepository;
    @Autowired
    private UserReadRepository userReadRepository;

    @BeforeEach
    public void setup() {
        userMongoRepository.deleteAll();
    }

    @Nested
    @DisplayName("Get should by email")
    class GetShouldByEmail {
        @Test
        @DisplayName("return user")
        void should_return_user() {
            User user = createUser();
            Optional<User> userOptional = userMongoRepository.findByEmail("email@email.com");
            assertFalse(userOptional.isEmpty());
            assertEquals(user, userOptional.get());
        }

        @Test
        @DisplayName("should not return user when its not there")
        void should_not_return_user_when_its_not_there() {
            Optional<User> user = userReadRepository.findByEmail("email");
            assertEquals(Optional.empty(), user);
        }
    }

    @Nested
    @DisplayName("Get should by Id")
    class GetShouldById {
        @Test
        @DisplayName("return user")
        void should_return_user_by_email() {
            User user = createUser();
            Optional<User> userOptional = userMongoRepository.findById(user.getId());
            assertFalse(userOptional.isEmpty());
            assertEquals(user, userOptional.get());
        }

        @Test
        @DisplayName("should not return user when its not there")
        void should_not_return_user_when_its_not_there() {
            Optional<User> user = userReadRepository.findByUserId("exampleUserId");
            assertEquals(Optional.empty(), user);
        }
    }

    private User createUser() {
        User user = User.builder().build();
        user.setPassword("password");
        user.setRoles(Set.of(Role.USER));
        user.setEmail("email@email.com");
        return userMongoRepository.save(user);
    }
}
