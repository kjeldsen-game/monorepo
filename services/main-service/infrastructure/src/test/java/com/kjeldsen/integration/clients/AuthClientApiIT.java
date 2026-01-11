package com.kjeldsen.integration.clients;

import com.kjeldsen.auth.domain.models.User;
import com.kjeldsen.auth.persistence.mongo.repositories.UserMongoRepository;
import com.kjeldsen.auth.rest.model.ServiceTokenRequest;
import com.kjeldsen.auth.rest.model.TokenResponse;
import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.lib.clients.AuthClientApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AuthClientApiIT extends AbstractIT {

    @Autowired
    private AuthClientApi authClientApi;
    @Autowired
    private UserMongoRepository userMongoRepository;

    @BeforeEach
    void setUp() {
        userMongoRepository.deleteAll();
        userMongoRepository.save(User.builder().build());
    }

    @Test
    @DisplayName("Should get a team")
    void should_make_a_request_to_the_backend() {
        TokenResponse result = authClientApi.getServiceToken(new ServiceTokenRequest(
            "main-service", "match"
        ));
        assertThat(result).isNull();
    }
}
