package com.kjeldsen.auth.authentication;

import com.kjeldsen.auth.authentication.model.ErrorResponse;
import com.kjeldsen.auth.domain.exceptions.BadRequestException;
import com.kjeldsen.auth.domain.exceptions.ForbiddenException;
import com.kjeldsen.auth.domain.exceptions.NotFoundException;
import com.kjeldsen.auth.domain.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        log.warn("BadRequestException: {}", ex.getMessage(), ex);
        return buildResponse("403", ex.getMessage(), "error", HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        log.warn("BadRequestException: {}", ex.getMessage(), ex);
        return buildResponse("400", ex.getMessage(), "error", HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        log.warn("NotFoundException: {}", ex.getMessage(), ex);
        return buildResponse("404", ex.getMessage(), "error", HttpStatus.NOT_FOUND, request.getRequestURI());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        log.warn("UnauthorizedException: {}", ex.getMessage(), ex);
        return buildResponse("401", ex.getMessage(), "error", HttpStatus.UNAUTHORIZED, request.getRequestURI());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
        log.warn("ForbiddenException: {}", ex.getMessage(), ex);
        return buildResponse("403", ex.getMessage(), "error", HttpStatus.FORBIDDEN, request.getRequestURI());
    }

    private ResponseEntity<ErrorResponse> buildResponse(String code, String message, String status, HttpStatus httpStatus, String path) {
        return ResponseEntity
            .status(httpStatus)
            .body(new ErrorResponse(code, message, status));
    }
}
