package com.kjeldsen.player.domain;


import com.kjeldsen.player.domain.events.TransactionEvent;
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
    TransactionId transactionId;
    Team.TeamId teamId;
    BigDecimal transactionAmount;
    BigDecimal prevTransactionBalance;
    BigDecimal postTransactionBalance;
    TransactionType transactionType;
    Instant occurredAt;

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
            .teamId(transactionEvent.getTeamId())
            .transactionType(transactionEvent.getTransactionType())
            .transactionId(TransactionId.generate())
            .transactionAmount(transactionEvent.getTransactionAmount())
            .prevTransactionBalance(transactionEvent.getPrevTransactionBalance())
            .postTransactionBalance(transactionEvent.getPostTransactionBalance())
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
