package com.kjeldsen.market.domain.repositories.queries;

import com.kjeldsen.market.domain.Auction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class FindAuctionsQuery {
    private Integer size;
    private Integer page;
    private String teamId;
    private List<Auction.AuctionStatus> auctionStatus;
    private Double minAverageBid;
    private Double maxAverageBid;
    private String playerId;

    // Player params
    private String preferredPosition;
    private Integer minAge;
    private Integer maxAge;
    private List<PlayerSkillFilter> skills;

    @Getter
    @Builder
    @Setter
    @ToString
    public static class PlayerSkillFilter {
        private String playerSkill;
        @Builder.Default
        private Integer minValue = 0;
        @Builder.Default
        private Integer maxValue = 100;
        @Builder.Default
        private Integer minPotentialValue = 0;
        @Builder.Default
        private Integer maxPotentialValue = 100;
    }
}
