package com.kjeldsen.match.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Match {

    Long id;
    Team home;
    Team away;
    MatchReport matchReport;
}
