package com.kjeldsen.player.persistence.adapters.mongo;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerStatus;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.FindPlayersQuery;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.queries.FilterMarketPlayersQuery;
import com.kjeldsen.player.persistence.mongo.repositories.PlayerMongoRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerReadRepositoryMongoAdapter implements PlayerReadRepository {

    private final PlayerMongoRepository playerMongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public List<Player> findByPlayersIds(List<String> playersIds) {
        return playerMongoRepository.findByPlayerIds(playersIds);
    }

    @Override
    public Optional<Player> findOneById(Player.PlayerId id) {
        return playerMongoRepository.findById(id);
    }

    @Override
    public List<Player> find(FindPlayersQuery query) {
        Example<Player> playerDocumentExample = Example.of(Player.builder()
            .position(query.getPosition() != null ? query.getPosition() : null)
            .build());
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());

        return playerMongoRepository.findAll(playerDocumentExample, pageable).stream().toList();
    }

    @Override
    public List<Player> filterMarketPlayers(FilterMarketPlayersQuery inputQuery) {
        Query query = new Query();
        if (inputQuery.getMinAge() != null || inputQuery.getMaxAge() != null) {
            Criteria ageCriteria = Criteria.where("age.years");
            if (inputQuery.getMinAge() != null) {
                ageCriteria = ageCriteria.gte(inputQuery.getMinAge());
            }
            if (inputQuery.getMaxAge() != null) {
                ageCriteria = ageCriteria.lte(inputQuery.getMaxAge());
            }
            query.addCriteria(ageCriteria);
        }

        query.addCriteria(Criteria.where("status").is(PlayerStatus.FOR_SALE));
        if (inputQuery.getPosition() != null) {
            System.out.println("position: " + inputQuery.getPosition());
            query.addCriteria(Criteria.where("position").is(inputQuery.getPosition()));
        }

        if (inputQuery.getPlayerIds() != null && !inputQuery.getPlayerIds().isEmpty()) {
            query.addCriteria(Criteria.where("_id").in(inputQuery.getPlayerIds()));
        }

        // Filter by Player skills
        if (inputQuery.getSkills() != null) {
            List<Criteria> criteriaList = inputQuery.getSkills().stream()
                .map(filter -> {
                    Criteria skillCriteria = Criteria.where("actualSkills." + filter.getPlayerSkill() + ".actual");
                    if (filter.getMinValue() != null ) { skillCriteria.gte(filter.getMinValue()); }
                    if (filter.getMaxValue() != null ) { skillCriteria.lte(filter.getMaxValue()); }
                    return skillCriteria;
                }).toList();

            if (!criteriaList.isEmpty()) {
                query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
            }
        }


        return mongoTemplate.find(query, Player.class);
    }

    @Override
    public List<Player> findByTeamId(Team.TeamId teamId) {
        return playerMongoRepository.findByTeamId(teamId);
    }

    @Override
    public List<Player> findPlayerUnderAge(Integer age) {
        return playerMongoRepository.findPlayerUnderAge(age);
    }

    @Override
    public List<Player> findPlayerOverAge(Integer age) {
        return playerMongoRepository.findPlayerOverAge(age);
    }
}
