package com.kjeldsen.auth.mongo.repositories;

import com.kjeldsen.auth.domain.events.SignUpEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpEventRepository extends MongoRepository<SignUpEvent, String> {

}
