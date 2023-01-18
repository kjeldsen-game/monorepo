package com.kjeldsen.match.domain.aggregate;

import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public class Play {

    private String playId;
    private Instant date;
    private List<Duel> duels;

}