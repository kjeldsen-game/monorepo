package com.kjeldsen.market.domain;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@Document(collection = "Auctions")
@TypeAlias("Auction")
public class Auction {
    AuctionId id;
    Player.PlayerId playerId;
    Team.TeamId teamId;
    BigDecimal averageBid;
    AuctionStatus status;
    Instant startedAt;
    Instant endedAt;
    List<Bid> bids;

    public void reduceEndedAtBySeconds(Integer time) {
        this.endedAt = endedAt.minusSeconds(time);
    }

    public record AuctionId(String value) {

        public static AuctionId generate() {
            return new AuctionId(UUID.randomUUID().toString());
        }

        public static AuctionId of(String value) {
            return new AuctionId(value);
        }
    }

    @Getter
    @Setter
    @Builder
    @ToString
    public static class Bid {
        Team.TeamId teamId;
        BigDecimal amount;
        Instant timestamp;
    }

    @Getter
    public enum AuctionStatus {
        COMPLETED,
        CANCEL,
        ACTIVE
    }
}
