package com.kjeldsen.match.domain.clients.models.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kjeldsen.match.domain.clients.deserializers.SkillsDeserializer;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@JsonDeserialize(using = SkillsDeserializer.class)
public class PlayerSkills {
    @JsonProperty("actual")
    private Integer actual;
    @JsonProperty("potential")
    private Integer potential;
    @JsonProperty("playerSkillRelevance")
    private String playerSkillRelevance;
}
