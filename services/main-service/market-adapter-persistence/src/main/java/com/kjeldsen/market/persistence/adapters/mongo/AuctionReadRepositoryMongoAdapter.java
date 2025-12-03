package com.kjeldsen.market.persistence.adapters.mongo;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.queries.FindAuctionsQuery;
import com.kjeldsen.market.persistence.mongo.repositories.AuctionMongoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuctionReadRepositoryMongoAdapter implements AuctionReadRepository {

    private final AuctionMongoRepository auctionMongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Auction> findAllByQuery(FindAuctionsQuery inputQuery) {
        Query query = new Query();
        if (inputQuery.getPlayerId() != null && inputQuery.getPlayerId().value() != null) {
            query.addCriteria(Criteria.where("playerId").is(inputQuery.getPlayerId().value()));
        }

        if (inputQuery.getMinAverageBid() != null || inputQuery.getMaxAverageBid() != null) {
            Criteria averageBidCriteria = Criteria.where("averageBid");
            if (inputQuery.getMinAverageBid() != null) {
                averageBidCriteria = averageBidCriteria
                        .gt(new Decimal128(BigDecimal.valueOf(inputQuery.getMinAverageBid())));
            }
            if (inputQuery.getMinAverageBid() != null) {
                averageBidCriteria = averageBidCriteria
                        .lt(new Decimal128(BigDecimal.valueOf(inputQuery.getMinAverageBid())));
            }
            query.addCriteria(averageBidCriteria);
        }

        if (inputQuery.getAuctionStatus() != null) {
            query.addCriteria(Criteria.where("status").is(inputQuery.getAuctionStatus()));
        }
        return mongoTemplate.find(query, Auction.class);
    }

    @Override
    public Optional<Auction> findById(Auction.AuctionId auctionId) {
        return auctionMongoRepository.findById(auctionId);
    }

    @Override
    public List<Auction> findAll() {
        return auctionMongoRepository.findAll();
    }
}
