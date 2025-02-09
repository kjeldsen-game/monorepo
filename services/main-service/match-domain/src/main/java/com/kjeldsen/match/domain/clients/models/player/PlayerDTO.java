package com.kjeldsen.match.domain.clients.models.player;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@Builder
public class PlayerDTO {
    String id;
    String teamId;
    String teamRole;

    String name;
    String position;
    String status;
    @JsonProperty("actualSkills")
    Map<String, PlayerSkills> actualSkills;
    String playerOrder;
}
