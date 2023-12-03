package com.kjeldsen.match.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kjeldsen.match.engine.state.GameState;
import com.kjeldsen.match.engine.state.GameStateException;
import com.kjeldsen.match.engine.entities.MatchReport;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlers {

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
            .map((GameState endState) -> new MatchReport(endState, endState.getPlays(), null, null))
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
