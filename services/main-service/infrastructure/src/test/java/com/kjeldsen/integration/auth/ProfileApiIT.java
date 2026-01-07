package com.kjeldsen.integration.auth;

import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.persistence.mongo.repositories.UserMongoRepository;
import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class ProfileApiIT extends AbstractIT {

    @Autowired
    private UserMongoRepository userMongoRepository;
    @Autowired
    private TeamMongoRepository teamMongoRepository;

    @BeforeEach
    void setUp() {
        teamMongoRepository.deleteAll();
        userMongoRepository.deleteAll();
    }


    @Nested
    @DisplayName("HTTP GET to /auth should")
    class HTTPGetToAuthShould {

        @Test
        @Disabled
        @DisplayName("return 200 when a user is authenticated")
        void return_201_status_when_user_is_authenticated() throws Exception {
            User user = User.builder().id("123").teamId("t").email("email@email.com").password("pass").build();
            userMongoRepository.save(user);
            teamMongoRepository.save(Team.builder().name("1234").userId("123").id(Team.TeamId.of("t")).build());

            log.info("User {} is authenticated", teamMongoRepository.findOneByUserId(user.getId()).get());

            String token = "exampleToken";
            try(MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
                mockedStatic.when(SecurityUtils::getCurrentUserId).thenReturn(user.getId());
                mockMvc.perform(get("/v1/auth/profile")
                        .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()));
            }
        }
    }
}
