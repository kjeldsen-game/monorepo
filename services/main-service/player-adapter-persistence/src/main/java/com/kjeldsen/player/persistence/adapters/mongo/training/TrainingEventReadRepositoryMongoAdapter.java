package com.kjeldsen.player.persistence.adapters.mongo.training;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.repositories.training.TrainingEventReadRepository;
import com.kjeldsen.player.persistence.mongo.repositories.training.TrainingEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrainingEventReadRepositoryMongoAdapter implements TrainingEventReadRepository {

    private final MongoTemplate mongoTemplate;
    private final TrainingEventMongoRepository trainingEventMongoRepository;

    @Override
    public List<TrainingEvent> findAllSuccessfulByTeamIdTypeOccurredAt(Team.TeamId teamId, TrainingEvent.TrainingType type, Instant since) {
        Criteria criteria = Criteria.where("teamId").is(teamId);
        Document expr = new Document("$ne", Arrays.asList("$pointsBeforeTraining", "$pointsAfterTraining"));
        criteria = criteria.andOperator(Criteria.where("$expr").is(expr));

        if (type != null) {
            criteria = criteria.and("type").is(type);
        }

        Query query = new Query(criteria);
        return mongoTemplate.find(query, TrainingEvent.class);
    }

    @Override
    public Optional<TrainingEvent> findLatestByPlayerIdAndType(Player.PlayerId playerId, TrainingEvent.TrainingType type) {
        return trainingEventMongoRepository.findFirstByPlayerIdAndTypeOrderByOccurredAtDesc(playerId, type);
    }

    @Override
    public Optional<TrainingEvent> findFirstByReferenceOrderByOccurredAtDesc(String reference) {
        return trainingEventMongoRepository.findFirstByReferenceOrderByOccurredAtDesc(reference);
    }
}
