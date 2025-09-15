package com.kjeldsen.player.domain.repositories.training;

import com.kjeldsen.player.domain.models.training.TrainingEvent;

public interface TrainingEventWriteRepository {

    TrainingEvent save(TrainingEvent trainingEvent);
}
