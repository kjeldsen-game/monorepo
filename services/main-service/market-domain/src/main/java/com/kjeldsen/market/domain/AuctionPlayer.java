package com.kjeldsen.market.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuctionPlayer {

    private String id;
    private String name;
    private String teamId;
    private String preferredPosition;
    private AuctionPlayerAge age;
    private Map<String, AuctionPlayerSkill> actualSkills;

    @Data
    public static class AuctionPlayerSkill {
        private Integer actual;
        private Integer potential;
    }

    @Data
    public static class AuctionPlayerAge {
        private Integer years;
        private Double months;
        private Double days;
    }
}
