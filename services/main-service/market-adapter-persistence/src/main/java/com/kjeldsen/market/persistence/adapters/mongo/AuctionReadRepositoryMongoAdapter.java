package com.kjeldsen.market.persistence.adapters.mongo;

import com.beust.ah.A;
import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.AuctionQuery;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.persistence.mongo.repositories.AuctionMongoRepository;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.FindTeamsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuctionReadRepositoryMongoAdapter implements AuctionReadRepository {

    private final AuctionMongoRepository auctionMongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Auction> findById(Auction.AuctionId auctionId) {
       return auctionMongoRepository.findById(auctionId);
    }

    @Override
    public List<Auction> findAll() {
        return auctionMongoRepository.findAll();
    }
}
