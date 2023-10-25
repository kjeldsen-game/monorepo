package com.kjeldsen.match.entities;

import com.kjeldsen.match.entities.duel.Duel;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Play {

    Id id;
    Action action;
    Duel duel;
    int minute;
}