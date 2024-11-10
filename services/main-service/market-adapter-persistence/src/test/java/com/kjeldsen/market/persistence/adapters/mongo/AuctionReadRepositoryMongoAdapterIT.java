package com.kjeldsen.market.persistence.adapters.mongo;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.queries.FindAuctionsQuery;
import com.kjeldsen.market.persistence.common.AbstractMongoDbTest;
import com.kjeldsen.market.persistence.mongo.repositories.AuctionMongoRepository;
import com.kjeldsen.player.domain.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

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
    }

    @Nested
    @DisplayName("Get should")
    class GetShould {
        @Test
        @DisplayName("Return empty list because the status is different")
        void return_stored_tendency_for_provided_position_when_exist_in_database() {
            auctionMongoRepository.save(
                Auction.builder().id(Auction.AuctionId.of("auctionId"))
                    .averageBid(BigDecimal.TEN).playerId(Player.PlayerId.generate()).status(Auction.AuctionStatus.CANCEL).bids(
                        List.of(Auction.Bid.builder().build(), Auction.Bid.builder().build()))
                    .build());


            List<Auction> getAuctions = auctionReadRepository.findAllByQuery(FindAuctionsQuery.builder().
                auctionStatus(Auction.AuctionStatus.COMPLETED).build());

            assertThat(getAuctions).isEmpty();
        }
    }
}