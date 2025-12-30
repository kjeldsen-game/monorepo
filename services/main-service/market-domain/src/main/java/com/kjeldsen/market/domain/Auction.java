package com.kjeldsen.market.domain;


import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

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
    String playerId;
    AuctionPlayer player;
    String teamId;
    String winnerTeamId;
    @Field(targetType = FieldType.DECIMAL128)
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
        String teamId;
        @Field(targetType = FieldType.DECIMAL128)
        BigDecimal amount;
        Instant timestamp;
    }

    @Getter
    public enum AuctionStatus {
        COMPLETED,
        CANCELED,
        ACTIVE
    }
}
