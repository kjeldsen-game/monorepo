package com.kjeldsen.match.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ScheduledMatch {
    private String homeTeamId;
    private String awayTeamId;
    private LocalDateTime date;
}