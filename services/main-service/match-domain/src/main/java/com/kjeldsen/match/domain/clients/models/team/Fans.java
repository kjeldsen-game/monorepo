package com.kjeldsen.match.domain.clients.models.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kjeldsen.match.domain.clients.deserializers.FansDeserializer;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonDeserialize(using = FansDeserializer.class)
public class Fans {

    @JsonProperty("fans")
    private Map<String, FanTier> fanTiers;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FanTier {
        private Integer totalFans;
        private Double loyalty;
    }

    public Integer getTotalFans() {
        return this.fanTiers.values().stream().mapToInt(FanTier::getTotalFans).sum();
    }
}
