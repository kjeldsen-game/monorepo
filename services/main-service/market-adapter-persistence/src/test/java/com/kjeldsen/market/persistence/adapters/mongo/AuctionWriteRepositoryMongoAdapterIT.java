package com.kjeldsen.market.persistence.adapters.mongo;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.persistence.common.AbstractMongoDbTest;
import com.kjeldsen.market.persistence.mongo.repositories.AuctionMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class AuctionWriteRepositoryMongoAdapterIT extends AbstractMongoDbTest {

    @Autowired
    private AuctionMongoRepository auctionMongoRepository;

    @Autowired
    private AuctionWriteRepositoryMongoAdapter auctionWriteRepositoryMongoAdapter;

    @BeforeEach
    public void setup() {
        auctionMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save auction to database")
    void should_save_auction_to_database() {
        Auction auction = auctionWriteRepositoryMongoAdapter.save(Auction.builder()
            .id(Auction.AuctionId.of("exampleId"))
            .averageBid(BigDecimal.ONE)
            .playerId("playerId")
            .build());

        Auction result = auctionMongoRepository.findById(auction.getId()).get();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("exampleId");
    }

}
