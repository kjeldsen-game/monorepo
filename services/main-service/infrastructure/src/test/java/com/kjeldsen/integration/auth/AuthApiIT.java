package com.kjeldsen.integration.auth;

import com.kjeldsen.auth.authentication.model.RegisterRequest;
import com.kjeldsen.auth.authentication.model.TokenRequest;
import com.kjeldsen.auth.authorization.SecurityUtils;
import com.kjeldsen.auth.domain.Role;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.providers.JwtTokenProvider;
import com.kjeldsen.auth.persistence.mongo.repositories.UserMongoRepository;
import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.persistence.mongo.repositories.TeamMongoRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Set;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthApiIT extends AbstractIT {

    @Autowired
    private UserMongoRepository userMongoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TeamMongoRepository teamMongoRepository;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

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
            User user = saveUser();
            Team team = Team.builder().id(Team.TeamId.of("exampleId")).userId(user.getId()).build();
            teamMongoRepository.save(team);

            String token = "exampleToken";
            try(MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
                mockedStatic.when(SecurityUtils::getCurrentUserId).thenReturn(user.getId());
                mockMvc.perform(get("/v1/auth/me")
                    .header("Authorization", "Bearer " + token))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(user.getId()));
            }
        }
    }

    @Nested
    @DisplayName("HTTP POST to /auth should")
    class HTTPPostToAuthShould {

        @Test
        @Disabled
        @DisplayName("return 200 and registered the new user")
        void return_200_when_user_register() throws Exception {
            RegisterRequest request =  new RegisterRequest().email("email@email.com").password("password"
            ).confirmPassword("password").teamName("team");

            mockMvc.perform(post("/v1/auth/register")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

            System.out.println(userMongoRepository.findByEmail("email@email.com").isPresent());

            Assertions.assertFalse(userMongoRepository.findAll().isEmpty());
        }

        @Test
        @DisplayName("return 400 error when request is invalid")
        void return_400_error_when_request_is_invalid() throws Exception {
            RegisterRequest request =  new RegisterRequest().email("email@email.com").password("password"
            ).confirmPassword("password22").teamName("team");

            mockMvc.perform(post("/v1/auth/register")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("return 200 and token")
        void return_200_and_token_for_registered_user() throws Exception {
            User user = saveUser();
            userMongoRepository.save(user);

            TokenRequest request = new TokenRequest().email(user.getEmail()).password("password");
            when(jwtTokenProvider.generateToken(user.getId(),user.getRoles())).thenReturn("exampleToken");

            mockMvc.perform(post("/v1/auth/token")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value("exampleToken"));
        }
    }

    private User saveUser() {
        User user = new User();
        user.setRoles(Set.of(Role.USER));
        user.setTeamId("exampleId");
        user.setEmail("test@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        return userMongoRepository.save(user);
    }
}
