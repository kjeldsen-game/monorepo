package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.persistence.mongo.documents.TeamDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMongoRepository extends MongoRepository<TeamDocument, String> {
}
