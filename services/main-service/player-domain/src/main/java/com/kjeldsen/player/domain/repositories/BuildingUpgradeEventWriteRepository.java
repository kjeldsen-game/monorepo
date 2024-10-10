package com.kjeldsen.player.domain.repositories;

import com.kjeldsen.player.domain.events.BuildingUpgradeEvent;

public interface BuildingUpgradeEventWriteRepository {
    BuildingUpgradeEvent save(BuildingUpgradeEvent buildingUpgradeEvent);

}
