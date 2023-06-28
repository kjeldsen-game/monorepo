package com.kjeldsen.player.integration.player;

import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.integration.AbstractIT;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class TrainingApiIT extends AbstractIT {
    @Autowired
    private PlayerReadRepository playerReadRepository;
    @Autowired
    private PlayerWriteRepository playerWriteRepository;
    @Autowired
    private PlayerMongoRepository playerMongoRepository;

    @BeforeEach
    void setUp() {
        playerMongoRepository.deleteAll();
    }

}
