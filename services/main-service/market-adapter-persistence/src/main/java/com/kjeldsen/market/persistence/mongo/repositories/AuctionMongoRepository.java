package com.kjeldsen.market.persistence.mongo.repositories;

import com.kjeldsen.market.domain.Auction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionMongoRepository extends MongoRepository<Auction, Auction.AuctionId> {

}
