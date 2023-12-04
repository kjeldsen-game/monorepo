package com.kjeldsen.match.engine.entities;

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

    String id;
    List<Player> players;

    Integer rating;

    Tactic tactic;
    VerticalPressure verticalPressure;
    HorizontalPressure horizontalPressure;

    @Override
    public String toString() {
        return JsonUtils.prettyPrint(this);
    }
}
