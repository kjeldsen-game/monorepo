package com.kjeldsen.player.rest.delegate;


import com.kjeldsen.player.rest.model.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandlerPlayer {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        Map<String, Object> body = Map.of(
            "code", 403,
            "message", "Forbidden: you do not have access to this resource"
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleBadRequest(RuntimeException ex) {
        log.warn("BadRequestException: {}", ex.getMessage(), ex);
        return buildResponse("400", ex.getMessage(), "error", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Error> buildResponse(String code, String message, String status, HttpStatus httpStatus) {
        return ResponseEntity
            .status(httpStatus)
            .body(new Error(Integer.parseInt(code), message));
    }
}
