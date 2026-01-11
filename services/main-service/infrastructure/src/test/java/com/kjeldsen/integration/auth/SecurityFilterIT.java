package com.kjeldsen.integration.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjeldsen.auth.rest.model.ServiceTokenRequest;
import com.kjeldsen.integration.config.QuartzTestConfig;
import com.kjeldsen.integration.TestApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.not;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles({"test-it"})
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({QuartzTestConfig.class})
public class SecurityFilterIT {

    @Autowired
    private MockMvc mockMvc;

    @Value("${internal.api.key}")
    private String internalApiKey;

    ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("Should test unprotected internal endpoints")
    class UnprotectedInternalEndpoint {

        private ResultActions performTokenServiceRequest(String internalHeader) throws Exception {
            var requestBuilder = post("/v1/auth/token-service")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                    new ServiceTokenRequest("client", "market")
                        .serviceName("auth")
                ));

            if (internalHeader != null) {
                requestBuilder.header("X-Internal-Request", internalHeader);
            }

            return mockMvc.perform(requestBuilder);
        }

        static Stream<Arguments> invalidInternalHeaderCases() {
            return Stream.of(
                Arguments.of((Object) null),
                Arguments.of("client")
            );
        }

        @ParameterizedTest
        @MethodSource("invalidInternalHeaderCases")
        void should_fail_unprotected_internal_endpoint_due_missing_header(String headerValue) throws Exception {
            performTokenServiceRequest(headerValue)
                .andExpect(status().isUnauthorized());
        }


        @Test
        void should_succeed_unprotected_internal_endpoint() throws Exception {
            performTokenServiceRequest(internalApiKey)
                .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Should test protected internal endpoints")
    class ProtectedInternalEndpoint {

        @Test
        void should_fail_if_protected_internal_endpoint_accessed_without_internal_header() throws Exception {
            mockMvc.perform(post("/v1/league")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void should_fail_if_protected_internal_endpoint_without_token() throws Exception {
            mockMvc.perform(post("/v1/league")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Internal-Request", internalApiKey))
                .andExpect(status().isUnauthorized());
        }

        @Test
        void should_not_fail_with_unauthorized_but_on_server() throws Exception {
            mockMvc.perform(
                    get("/v1/internal")
                        .with(jwt().jwt(jwt -> jwt.claim("type", "internal")))
                        .header("X-Internal-Request", internalApiKey)
                )
                .andExpect(status().is(not(HttpStatus.UNAUTHORIZED.value())));
        }
    }

    @Nested
    @DisplayName("Should test protected public/internal endpoints")
    class ProtectedPublicInternalEndpoint {
        @Test
        void should_fail_on_unauthorized_if_no_token_provided() throws Exception {
            mockMvc.perform(post("/v1/auth/me")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        }
    }
}
