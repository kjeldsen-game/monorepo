package com.kjeldsen.player.persistence.adapters.mongo.training;

import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.domain.repositories.training.TrainingEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.training.TrainingEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingEventWriteRepositoryMongoAdapter implements TrainingEventWriteRepository {

    private final TrainingEventMongoRepository trainingEventMongoRepository;

    @Override
    public TrainingEvent save(TrainingEvent trainingEvent) {
        return trainingEventMongoRepository.save(trainingEvent);
    }
}
