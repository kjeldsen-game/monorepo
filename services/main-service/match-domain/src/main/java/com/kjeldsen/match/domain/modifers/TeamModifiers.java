package com.kjeldsen.match.domain.modifers;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamModifiers {

    VerticalPressure verticalPressure;
    HorizontalPressure horizontalPressure;
    Tactic tactic;

    public TeamModifiers deepCopy() {
        return new TeamModifiers(verticalPressure, horizontalPressure, tactic);
    }
}
