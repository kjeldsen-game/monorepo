package com.kjeldsen.market.persistence.adapters.mongo;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.AuctionPlayer;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.queries.FindAuctionsQuery;
import com.kjeldsen.market.persistence.common.AbstractMongoDbTest;
import com.kjeldsen.market.persistence.mongo.repositories.AuctionMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Component.class))
@ActiveProfiles("test")
public class AuctionReadRepositoryMongoAdapterIT extends AbstractMongoDbTest {

    @Autowired
    private AuctionMongoRepository auctionMongoRepository;

    @Autowired
    private AuctionReadRepository auctionReadRepository;

    @BeforeEach
    public void setup() {
        auctionMongoRepository.deleteAll();
        auctionMongoRepository.saveAll(List.of(
            Auction.builder().id(Auction.AuctionId.of("1"))
                .status(Auction.AuctionStatus.ACTIVE)
                .player(AuctionPlayer.builder().id("player1").build())
                .averageBid(BigDecimal.ZERO)
                .bids(List.of())
                .build(),
            Auction.builder().id(Auction.AuctionId.of("2"))
                .status(Auction.AuctionStatus.COMPLETED)
                .player(AuctionPlayer.builder().id("player43").build())
                .averageBid(BigDecimal.ZERO)
                .bids(List.of())
                .build(),
            Auction.builder().id(Auction.AuctionId.of("3"))
                .status(Auction.AuctionStatus.COMPLETED)
                .player(AuctionPlayer.builder().id("player43").build())
                .averageBid(BigDecimal.ZERO)
                .bids(List.of())
                .build()
            ));
    }


    @Nested
    @DisplayName("Get should")
    class GetShould {

        static Stream<Arguments> auctionStatusAndExpected() {
            return Stream.of(
                Arguments.of(Auction.AuctionStatus.ACTIVE, 1),
                Arguments.of(Auction.AuctionStatus.COMPLETED, 2),
                Arguments.of(Auction.AuctionStatus.CANCELED, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("auctionStatusAndExpected")
        @DisplayName("Return list of auctions that are completed")
        void return_auctions_that_are_completed(Auction.AuctionStatus status, int expected) {

            List<Auction> getAuctions = auctionReadRepository.findAllByQuery(
                FindAuctionsQuery.builder().auctionStatus(List.of(status)).build());

            assertThat(getAuctions).hasSize(expected);
        }


        static Stream<Arguments> playerIdAndExpectedCount() {
            return Stream.of(
                Arguments.of("player1", 1),
                Arguments.of("player43", 2),
                Arguments.of("player3", 0)
            );
        }

        @ParameterizedTest
        @MethodSource("playerIdAndExpectedCount")
        @DisplayName("Return auctions of playerId")
        void return_auctions_of_playerId(String playerId, int expectedCount) {

            List<Auction> getAuctions = auctionReadRepository.findAllByQuery(
                FindAuctionsQuery.builder().playerId(playerId).build());

            assertThat(getAuctions).hasSize(expectedCount);
        }
    }
}