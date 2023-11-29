package com.kjeldsen.match.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kjeldsen.match.engine.processing.ReportService;
import com.kjeldsen.match.models.MatchReport;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.GameStateException;
import java.util.Optional;
import javax.naming.AuthenticationException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlers {

    private final ReportService reportService;

    @Value
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ResponseError {

        String message;
        MatchReport report;

        public ResponseError(String message) {
            this.message = message;
            this.report = null;
        }
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(BadCredentialsException e) {
        ResponseError error = new ResponseError(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(AuthenticationException e) {
        ResponseError error = new ResponseError(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Error: {}", e.getMessage());
        e.printStackTrace();
        ResponseError error = new ResponseError("Something went wrong");
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleException(ValidationException e) {
        log.error("Validation error: {}", e.getMessage());
        e.printStackTrace();
        ResponseError error = new ResponseError(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(GameStateException.class)
    public ResponseEntity<?> handleException(GameStateException e) {
        log.error("Game state error: {}", e.getMessage());
        e.printStackTrace();
        return Optional.ofNullable(e.getState())
            .map((GameState endState) -> reportService.generateReport(endState, null, null))
            .map(report -> {
                ResponseError error = new ResponseError(e.getMessage(), report);
                return ResponseEntity.unprocessableEntity().body(error);
            })
            .orElseGet(() -> {
                ResponseError error = new ResponseError(e.getMessage());
                return ResponseEntity.unprocessableEntity().body(error);
            });
    }
}
