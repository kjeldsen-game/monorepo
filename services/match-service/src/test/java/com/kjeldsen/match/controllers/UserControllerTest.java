package com.kjeldsen.match.controllers;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjeldsen.match.security.AuthService;
import com.kjeldsen.match.models.User;
import com.kjeldsen.match.repositories.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;
    @MockBean
    AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // TODO sort out security
    @SneakyThrows
    @Test
    @Disabled
    void createUserReturnsOk() {
        User user = User.builder().id(1L).email("user@example.com").password("test").build();
        RequestBuilder request =
            post("/users")
                .content(toJson(user))
                .contentType("application/json");
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().json(toJson(user)));
    }

    // TODO this isn't reading from the database
    @Test
    @SneakyThrows
    @SqlGroup({
        @Sql(value = "classpath:h2/init.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:h2/reset.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)})
    @Disabled
    void readUserReturnsOk() {
        User user = User.builder().id(1L).email("alice@example.com").build();
        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(toJson(user)));
    }

    @SneakyThrows
    public String toJson(Object object) {
        return objectMapper.writeValueAsString(object);
    }
}