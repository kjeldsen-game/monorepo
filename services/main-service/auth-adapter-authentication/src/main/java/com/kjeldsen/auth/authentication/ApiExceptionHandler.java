package com.kjeldsen.auth.authentication;

import com.kjeldsen.auth.authentication.model.ErrorResponse;
import com.kjeldsen.auth.domain.exceptions.BadRequestException;
import com.kjeldsen.auth.domain.exceptions.ForbiddenException;
import com.kjeldsen.auth.domain.exceptions.NotFoundException;
import com.kjeldsen.auth.domain.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        log.warn("BadRequestException: {}", ex.getMessage(), ex);
        return buildResponse("400", ex.getMessage(), "error", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        log.warn("NotFoundException: {}", ex.getMessage(), ex);
        return buildResponse("404", ex.getMessage(), "error", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        log.warn("UnauthorizedException: {}", ex.getMessage(), ex);
        return buildResponse("401", ex.getMessage(), "error", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        log.warn("ForbiddenException: {}", ex.getMessage(), ex);
        return buildResponse("403", ex.getMessage(), "error", HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ErrorResponse> buildResponse(String code, String message, String status, HttpStatus httpStatus) {
        return ResponseEntity
            .status(httpStatus)
            .body(new ErrorResponse(code, message, status));
    }
}
