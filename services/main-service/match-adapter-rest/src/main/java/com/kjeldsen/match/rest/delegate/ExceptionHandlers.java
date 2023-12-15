package com.kjeldsen.match.rest.delegate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kjeldsen.match.entities.MatchReport;
import com.kjeldsen.match.state.GameState;
import com.kjeldsen.match.state.GameStateException;
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
