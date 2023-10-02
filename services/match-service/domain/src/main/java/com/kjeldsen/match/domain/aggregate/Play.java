package com.kjeldsen.match.domain.aggregate;

import com.kjeldsen.match.domain.id.PlayId;
import com.kjeldsen.match.domain.type.Action;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Play {

    PlayId playId;
    Action action;
    Duel duel;
    int minute;
}