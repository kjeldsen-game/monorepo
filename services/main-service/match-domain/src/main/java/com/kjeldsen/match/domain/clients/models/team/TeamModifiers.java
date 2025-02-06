package com.kjeldsen.match.domain.clients.models.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamModifiers {
    @JsonProperty("tactic")
    private String tactic;

    @JsonProperty("verticalPressure")
    private String verticalPressure;

    @JsonProperty("horizontalPressure")
    private String horizontalPressure;
}
