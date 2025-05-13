package com.kjeldsen.lib.model.team;

import lombok.*;

import java.math.BigDecimal;
import java.util.Map;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class BuildingsClient {
    private Integer freeSlots;
    private Map<String, FacilityDataClient> facilities;
    private StadiumClient stadium;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class FacilityDataClient {
        private Integer level;
        private BigDecimal maintenanceCost;

    }

    @ToString
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StadiumClient extends FacilityDataClient {
        private Integer seats;
    }

}
