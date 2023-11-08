package com.kjeldsen.match.controllers;

import com.kjeldsen.match.engine.exceptions.GameStateException;
import com.kjeldsen.match.engine.processing.Report;
import com.kjeldsen.match.engine.state.GameState;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlers {

    record ResponseError(String message, Report report) {

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Error: {}", e.getMessage());
        e.printStackTrace();
        ResponseError error = new ResponseError("Something went wrong", null);
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleException(ValidationException e) {
        log.error("Validation error: {}", e.getMessage());
        e.printStackTrace();
        ResponseError error = new ResponseError(e.getMessage(), null);
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(GameStateException.class)
    public ResponseEntity<?> handleException(GameStateException e) {
        log.error("Game state error: {}", e.getMessage());
        e.printStackTrace();
        return Optional.ofNullable(e.getState())
            .map((GameState endState) -> new Report(endState, null, null))
            .map(report -> {
                ResponseError error = new ResponseError(e.getMessage(), report);
                return ResponseEntity.internalServerError().body(error);
            })
            .orElseGet(() -> {
                ResponseError error = new ResponseError(e.getMessage(), null);
                return ResponseEntity.internalServerError().body(error);
            });
    }
}
