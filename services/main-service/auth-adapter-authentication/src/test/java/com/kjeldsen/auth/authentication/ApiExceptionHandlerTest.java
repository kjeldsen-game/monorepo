package com.kjeldsen.auth.authentication;

import com.kjeldsen.auth.domain.exceptions.*;
import com.kjeldsen.auth.rest.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiExceptionHandlerTest {

    private ApiExceptionHandler exceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ApiExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/test");
    }

    @Test
    @DisplayName("BadRequest returns 400")
    void should_handle_bad_request_exception() {
        BadRequestException ex = new BadRequestException("Invalid input");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBadRequest(ex, request);
        assert_error_response(response, 400, "400", "Invalid input", "error");
    }

    @Test
    @DisplayName("NotFound returns 404")
    void should_handle_not_found_exception() {
        NotFoundException ex = new NotFoundException("Resource not found");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNotFound(ex, request);
        assert_error_response(response, 404, "404", "Resource not found", "error");
    }

    @Test
    @DisplayName("Unauthorized returns 401")
    void should_handle_unauthorized_exception() {
        UnauthorizedException ex = new UnauthorizedException("Unauthorized access");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUnauthorized(ex, request);
        assert_error_response(response, 401, "401", "Unauthorized access", "error");
    }

    @Test
    @DisplayName("Forbidden returns 403")
    void should_handle_forbidden_exception() {
        ForbiddenException ex = new ForbiddenException("Forbidden access");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleForbidden(ex, request);
        assert_error_response(response, 403, "403", "Forbidden access", "error");
    }

    @Test
    @DisplayName("AccessDenied returns 400")
    void should_handle_access_denied_exception() {
        AccessDeniedException ex = new AccessDeniedException("Access denied");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccessDenied(ex, request);
        assert_error_response(response, 400, "403", "Access denied", "error");
    }

    private void assert_error_response(ResponseEntity<ErrorResponse> response, int httpStatus, String code, String message, String status) {
        assertNotNull(response, "ResponseEntity should not be null");
        assertEquals(httpStatus, response.getStatusCodeValue(), "HTTP status code should match");

        ErrorResponse body = response.getBody();
        assertNotNull(body, "ErrorResponse body should not be null");
        assertEquals(code, body.getCode(), "Error code should match");
        assertEquals(message, body.getMessage(), "Error message should match");
        assertEquals(status, body.getStatus(), "Error status should match");
    }
}
