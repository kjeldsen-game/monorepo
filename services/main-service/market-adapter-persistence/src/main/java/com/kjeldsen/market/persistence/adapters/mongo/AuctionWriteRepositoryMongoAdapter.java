package com.kjeldsen.market.persistence.adapters.mongo;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionWriteRepository;
import com.kjeldsen.market.persistence.mongo.repositories.AuctionMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionWriteRepositoryMongoAdapter implements AuctionWriteRepository {

    private final AuctionMongoRepository auctionMongoRepository;

    @Override
    public Auction save(final Auction auction) {
        return auctionMongoRepository.save(auction);
    }
}
