package com.kjeldsen.player.domain;

import com.kjeldsen.lib.events.TransactionEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    @DisplayName("Should create different TransactionIds")
    void should_create_different_auctionIds() {
        Transaction.TransactionId transactionId1 = Transaction.TransactionId.generate();
        Transaction.TransactionId transactionId2 = Transaction.TransactionId.generate();
        assertNotEquals(transactionId1, transactionId2);
    }

    @Test
    @DisplayName("Should assign right Id value with of")
    void should_assign_right_id_value_with_of() {
        String exampleId = "exampleId";
        Transaction.TransactionId transactionId = Transaction.TransactionId.of(exampleId);
        assertEquals(exampleId, transactionId.value());
    }

    @Test
    @DisplayName("Should create a Transaction from TransactionEvent")
    public void should_create_a_Transaction_from_TransactionEvent() {
        com.kjeldsen.lib.events.TransactionEvent testTransactionEvent =  TransactionEvent.builder()
            .teamId("example")
            .transactionType(Transaction.TransactionType.PLAYER_SALE.name())
            .transactionAmount(BigDecimal.TEN)
            .prevTransactionBalance(BigDecimal.ZERO)
            .postTransactionBalance(BigDecimal.TEN)
            .build();

        Transaction transaction = Transaction.creation(testTransactionEvent);

        assertEquals(testTransactionEvent.getTransactionType(), transaction.getTransactionType().name());
        assertEquals(testTransactionEvent.getTeamId(), transaction.getTeamId().value());
        assertEquals(testTransactionEvent.getTransactionAmount(), transaction.getTransactionAmount());
        assertEquals(testTransactionEvent.getPrevTransactionBalance(), transaction.getPrevTransactionBalance());
        assertEquals(testTransactionEvent.getPostTransactionBalance(), transaction.getPostTransactionBalance());

    }
}