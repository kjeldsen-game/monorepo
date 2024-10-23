package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.FansEvent;
import com.kjeldsen.player.domain.repositories.FansEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.FansEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FansEventWriteRepositoryMongoAdapter implements FansEventWriteRepository {

    private final FansEventMongoRepository fansEventWriteRepository;

    @Override
    public FansEvent save(FansEvent fansEvent) {
        return fansEventWriteRepository.save(fansEvent);
    }
}
