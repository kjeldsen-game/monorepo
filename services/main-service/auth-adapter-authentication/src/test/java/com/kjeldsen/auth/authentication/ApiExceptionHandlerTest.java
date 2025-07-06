package com.kjeldsen.auth.authentication;

import com.kjeldsen.auth.authentication.model.ErrorResponse;
import com.kjeldsen.auth.domain.exceptions.BadRequestException;
import com.kjeldsen.auth.domain.exceptions.ForbiddenException;
import com.kjeldsen.auth.domain.exceptions.NotFoundException;
import com.kjeldsen.auth.domain.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {

    static ApiExceptionHandler apiExceptionHandler;

    @BeforeAll
    static void setUp() {
        apiExceptionHandler = new ApiExceptionHandler();
    }

    @Test
    @DisplayName("Should return build bad request response ")
    void should_return_build_bad_request_response() {
        BadRequestException ex = new BadRequestException("Bad Request");
        ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleBadRequest(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Bad Request");
        assertThat(response.getBody().getStatus()).isEqualTo("error");
        assertThat(response.getBody().getCode()).isEqualTo("400");
    }

    @Test
    @DisplayName("Should build not found response")
    void should_build_not_found_response() {
        NotFoundException ex = new NotFoundException("Not Found");
        ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Not Found");
        assertThat(response.getBody().getStatus()).isEqualTo("error");
        assertThat(response.getBody().getCode()).isEqualTo("404");
    }

    @Test
    @DisplayName("Should build unauthorized response")
    void should_build_unauthorized_response() {
        UnauthorizedException ex = new UnauthorizedException("Unauthorized");
        ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleUnauthorized(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Unauthorized");
        assertThat(response.getBody().getStatus()).isEqualTo("error");
        assertThat(response.getBody().getCode()).isEqualTo("401");
    }

    @Test
    @DisplayName("Should build forbidden response")
    void should_build_forbidden_response() {
        ForbiddenException ex = new ForbiddenException("Forbidden");
        ResponseEntity<ErrorResponse> response = apiExceptionHandler.handleForbidden(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Forbidden");
        assertThat(response.getBody().getStatus()).isEqualTo("error");
        assertThat(response.getBody().getCode()).isEqualTo("403");
    }
}