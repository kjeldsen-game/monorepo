package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.entities.Match;
import com.kjeldsen.match.entities.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Player> findPlayersByMatchId(String matchId) {
        List<Player> result = new ArrayList<>();

        Match match = mongoTemplate.findById(matchId, Match.class);

        result.addAll(match.getHome().getPlayers());
        result.addAll(match.getHome().getBench());
        result.addAll(match.getAway().getPlayers());
        result.addAll(match.getAway().getBench());

        return result;
    }

    public void updatePlayer(String matchId, Player player) {
        // Consulta para encontrar el jugador en el equipo 'home' o 'away'
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("home.players.playerId").is(player.getId()),
                Criteria.where("away.players.playerId").is(player.getId())
        ));
        /*
        Update update = new Update();
        update.set("home.players.$[elem]", player); // Para el equipo 'home'
        update.set("away.players.$[elem]", player); // Para el equipo 'away'

        // Actualizar el jugador en el equipo 'home'
        mongoTemplate.updateMulti(query, update, Match.class, "home.players");

        // Actualizar el jugador en el equipo 'away'
        mongoTemplate.updateMulti(query, update, Match.class, "away.players");
        */
    }
}
