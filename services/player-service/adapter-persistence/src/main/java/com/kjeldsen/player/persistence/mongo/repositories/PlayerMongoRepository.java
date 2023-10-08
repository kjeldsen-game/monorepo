package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMongoRepository extends MongoRepository<Player, Player.PlayerId> {

    List<Player> findByTeamId(Team.TeamId teamId);

}
