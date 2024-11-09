package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.events.BuildingUpgradeEvent;
import com.kjeldsen.player.domain.repositories.BuildingUpgradeEventWriteRepository;
import com.kjeldsen.player.persistence.mongo.repositories.BuildingUpgradeEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BuildingUpgradeEventWriteRepositoryMongoAdapter implements BuildingUpgradeEventWriteRepository {

    private final BuildingUpgradeEventMongoRepository buildingUpgradeEventMongoRepository;

    @Override
    public BuildingUpgradeEvent save(BuildingUpgradeEvent buildingUpgradeEvent) {
        return buildingUpgradeEventMongoRepository.save(buildingUpgradeEvent);
    }
}
