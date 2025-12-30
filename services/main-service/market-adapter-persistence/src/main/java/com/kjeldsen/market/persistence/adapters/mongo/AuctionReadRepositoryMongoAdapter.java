package com.kjeldsen.market.persistence.adapters.mongo;

import com.kjeldsen.market.domain.Auction;
import com.kjeldsen.market.domain.repositories.AuctionReadRepository;
import com.kjeldsen.market.domain.repositories.queries.FindAuctionsQuery;
import com.kjeldsen.market.persistence.mongo.repositories.AuctionMongoRepository;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.queries.FindTeamsQuery;
import lombok.RequiredArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.data.domain.*;
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
    public Page<Auction> findAllByQueryPaged(FindAuctionsQuery query) {
        Query q = new Query();
        q.addCriteria(Criteria.where("status").in(query.getAuctionStatus()));
        q.addCriteria(new Criteria().orOperator(
            Criteria.where("teamId").is(query.getTeamId()),
            Criteria.where("bids").elemMatch(Criteria.where("teamId").is(query.getTeamId()))
        ));
        long total = mongoTemplate.count(q, Auction.class);
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());
        q.with(pageable);
        System.out.println("Total auctions found: " + total);
        List<Auction> auctions = mongoTemplate.find(q, Auction.class);
        return new PageImpl<>(auctions, pageable, total);
    }

    @Override
    public List<Auction> findAllByQuery(FindAuctionsQuery inputQuery) {
        Query query = new Query();
        if (inputQuery.getPlayerId() != null) {
            query.addCriteria(Criteria.where("player._id").is(inputQuery.getPlayerId()));
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
            query.addCriteria(Criteria.where("status").in(inputQuery.getAuctionStatus()));
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
