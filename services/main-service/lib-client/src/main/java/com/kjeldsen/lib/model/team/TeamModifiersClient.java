package com.kjeldsen.lib.model.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamModifiersClient {
    @JsonProperty("tactic")
    private String tactic;

    @JsonProperty("verticalPressure")
    private String verticalPressure;

    @JsonProperty("horizontalPressure")
    private String horizontalPressure;
}
