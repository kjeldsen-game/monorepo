package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.persistence.mongo.documents.PlayerDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerMongoRepository extends MongoRepository<PlayerDocument, String> {

}
