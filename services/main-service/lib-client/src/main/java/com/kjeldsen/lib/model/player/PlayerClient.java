package com.kjeldsen.lib.model.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerClient {
    String id;
    String teamId;
    String teamRole;

    String name;
    String position;
    String status;
    @JsonProperty("actualSkills")
    Map<String, PlayerSkillsClient> actualSkills;
    String playerOrder;
    String playerOrderDestinationPitchArea;
}
