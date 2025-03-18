package com.kjeldsen.match.domain.clients.models.team;

import com.kjeldsen.player.domain.Team;
import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class Buildings {
    private Integer freeSlots;
    private Map<String, FacilityData> facilities;
    private Stadium stadium;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class FacilityData {
        private Integer level;
        private BigDecimal maintenanceCost;

    }

    @ToString
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Stadium extends Team.Buildings.FacilityData {
        private Integer seats;
    }
}
