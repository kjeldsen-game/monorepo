package com.kjeldsen.auth.persistence.adapters.mongo;

import com.kjeldsen.auth.domain.models.User;
import com.kjeldsen.auth.persistence.mongo.repositories.UserMongoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReadRepositoryMongoAdapterTest {

    @Mock
    private UserMongoRepository userMongoRepository;

    @InjectMocks
    private UserReadRepositoryMongoAdapter userReadRepositoryMongoAdapter;

    @Test
    void shouldCallRepositoryFindByEmail() {
        String email = "test@example.com";
        User user = User.builder().build();
        when(userMongoRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userReadRepositoryMongoAdapter.findByEmail(email);

        assertThat(result).contains(user);
        verify(userMongoRepository).findByEmail(email);
    }

    @Test
    void shouldCallRepositoryFindById() {
        String userId = "abc123";
        User user = User.builder().build();
        when(userMongoRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userReadRepositoryMongoAdapter.findByUserId(userId);

        assertThat(result).contains(user);
        verify(userMongoRepository).findById(userId);
    }
}