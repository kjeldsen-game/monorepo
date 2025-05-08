package com.kjeldsen.player.domain;


import com.kjeldsen.lib.events.TransactionEvent;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@Document(collection = "Transactions")
@TypeAlias("Transaction")
public class Transaction {

    @Id
    TransactionId id;
    Team.TeamId teamId;
    BigDecimal transactionAmount;
    BigDecimal prevTransactionBalance;
    BigDecimal postTransactionBalance;
    TransactionType transactionType;
    Instant occurredAt;
    String message;

    public record TransactionId(String value) {
        public static TransactionId generate() {
            return new TransactionId(java.util.UUID.randomUUID().toString());
        }

        public static TransactionId of(String id) {
            return new TransactionId(id);
        }
    }

    public static Transaction creation(TransactionEvent transactionEvent) {
        return Transaction.builder()
            .occurredAt(Instant.now())
            .teamId(Team.TeamId.of(transactionEvent.getTeamId()))
            .transactionType(TransactionType.valueOf(transactionEvent.getTransactionType()))
            .id(TransactionId.generate())
            .transactionAmount(transactionEvent.getTransactionAmount())
            .prevTransactionBalance(transactionEvent.getPrevTransactionBalance())
            .postTransactionBalance(transactionEvent.getPostTransactionBalance())
            .message(transactionEvent.getMessage() != null ? transactionEvent.getMessage() : "")
            .build();
    }

    @Getter
    public enum TransactionType {
        SPONSOR,
        PLAYER_SALE,
        PLAYER_PURCHASE,
        ATTENDANCE,
        MERCHANDISE,
        PLAYER_WAGE,
        BUILDING_UPGRADE,
        BUILDING_MAINTENANCE,
        RESTAURANT,
        BILLBOARDS
    }
}
