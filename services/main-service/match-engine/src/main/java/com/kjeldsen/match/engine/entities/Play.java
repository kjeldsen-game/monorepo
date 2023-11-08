package com.kjeldsen.match.engine.entities;

import com.kjeldsen.match.engine.entities.duel.Duel;
import com.kjeldsen.match.utils.JsonUtils;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Play {

    Long id;
    Action action;
    Duel duel;
    int minute;

    @Override
    public String toString() {
        return JsonUtils.prettyPrintExclude(this, "*.skills");
    }
}
