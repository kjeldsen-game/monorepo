package com.kjeldsen.player.persistence.mongo.repositories;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMongoRepository extends MongoRepository<Player, Player.PlayerId> {

    List<Player> findByTeamId(Team.TeamId teamId);

    @Query("{ '_id.value' : { '$in' : ?0 } }")
    List<Player> findByPlayerIds(List<String> playerIds);

    @Query(value = "{ 'age.years': { $lt: ?0 } }")
    List<Player> findPlayerUnderAge(Integer age);

    @Query(value = "{ 'age.years': { $gte: ?0 }, 'status': { $ne: 'RETIRED' } }")
    List<Player> findPlayerOverAge(Integer age);
}
