package com.kjeldsen.notification.persistence.mongo.repositories;

import com.kjeldsen.notification.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationMongoRepository extends MongoRepository<Notification, String> {

        List<Notification> findByTeamIdAndIsReadOrderByCreatedAtDesc(String teamId, boolean isRead);

}
