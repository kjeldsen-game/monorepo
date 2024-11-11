package com.kjeldsen.integration.market;

import com.kjeldsen.integration.AbstractIT;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.market.persistence.mongo.repositories.AuctionMongoRepository;
import com.kjeldsen.market.rest.model.AuctionResponse;
import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MarketApiIT extends AbstractIT {

    @Autowired
    private AuctionWriteRepository auctionWriteRepository;
    @Autowired
    private AuctionMongoRepository auctionMongoRepository;

    @BeforeEach
    void setup() {
        auctionMongoRepository.deleteAll();
    }

    @Test
    @DisplayName("HTTP get to /market/auction/{marketId}")
    public void should_return_200_when_getting_auction_by_market_id() throws Exception {
        Auction exampleAuction = Auction.builder().id(Auction.AuctionId.of("auction"))
            .bids(new ArrayList<>()).playerId(Player.PlayerId.of("player")).teamId(Team.TeamId.of("team")).build();

        auctionWriteRepository.save(exampleAuction);

        AuctionResponse response = new AuctionResponse().playerId("player")
            .id("auction").bids(new ArrayList<>()).teamId("team");

        mockMvc.perform(get("/v1/market/auction/{marketId}", exampleAuction.getId().value()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
