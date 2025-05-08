package com.kjeldsen.lib.model.team;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.kjeldsen.lib.deserializers.FansDeserializerClient;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonDeserialize(using = FansDeserializerClient.class)
public class FansClient {

    @JsonProperty("fans")
    private Map<String, FanTierClient> fanTiers;

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FanTierClient {
        private Integer totalFans;
        private Double loyalty;
    }

    public Integer getTotalFans() {
        return this.fanTiers.values().stream().mapToInt(FanTierClient::getTotalFans).sum();
    }
}
