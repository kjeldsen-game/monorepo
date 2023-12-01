package com.kjeldsen.match.models;

import com.kjeldsen.match.engine.modifers.HorizontalPressure;
import com.kjeldsen.match.engine.modifers.Tactic;
import com.kjeldsen.match.engine.modifers.VerticalPressure;
import com.kjeldsen.match.utils.JsonUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    Long id;
    List<Player> players;

    Integer rating;

    // Modifiers - TODO these need to be passed as part of the match configuration as opposed to
    // being part of the team definition
    Tactic tactic;
    VerticalPressure verticalPressure;
    HorizontalPressure horizontalPressure;

    @Override
    public String toString() {
        return JsonUtils.prettyPrint(this);
    }
}
