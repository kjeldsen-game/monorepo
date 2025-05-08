package com.kjeldsen.lib.model.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kjeldsen.lib.deserializers.SkillsDeserializerClient;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@JsonDeserialize(using = SkillsDeserializerClient.class)
public class PlayerSkillsClient {
    @JsonProperty("actual")
    private Integer actual;
    @JsonProperty("potential")
    private Integer potential;
    @JsonProperty("playerSkillRelevance")
    private String playerSkillRelevance;
}
