package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.TeamModifiers;
import com.kjeldsen.player.domain.events.TeamCreationEvent;
import com.kjeldsen.player.domain.publishers.TeamCreationEventPublisher;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateTeamUseCase {

    private final GeneratePlayersUseCase generatePlayersUseCase;
    private final TeamWriteRepository teamWriteRepository;
    private final PlayerWriteRepository playerWriteRepository;
    private final TeamCreationEventPublisher teamCreationEventPublisher;
    private final TeamReadRepository teamReadRepository;

    public void create(String teamName, Integer numberOfPlayers, String userId) {
        log.info("CreateTeamUseCase name {} with {} players for user {}", teamName, numberOfPlayers, userId);

        Team.TeamId newTeamId = Team.TeamId.generate();
//        List<Player> players = generatePlayersUseCase.generate(numberOfPlayers, newTeamId);
        List<Player> players = generatePlayersUseCase.generateCustomPlayers(newTeamId);

        Team team = Team.builder()
            .id(newTeamId)
            .userId(userId)
            .name(teamName)
            .cantera(Team.Cantera.builder()
                .score(0.0)
                .economyLevel(0)
                .traditionLevel(0)
                .buildingsLevel(0)
                .build())
            .teamModifiers(TeamModifiers.builder()
                .horizontalPressure(TeamModifiers.getRandomValueTeamModifier(TeamModifiers.HorizontalPressure.class))
                .verticalPressure(TeamModifiers.getRandomValueTeamModifier(TeamModifiers.VerticalPressure.class))
                .tactic(TeamModifiers.getRandomValueTeamModifier(TeamModifiers.Tactic.class)).build())
            .economy(Team.Economy.builder()
                .balance(BigDecimal.valueOf(1_000_000))
                .prices( new HashMap<>(Map.of(
                    Team.Economy.PricingType.SEASON_TICKET, 14,
                    Team.Economy.PricingType.DAY_TICKET, 14,
                    Team.Economy.PricingType.MERCHANDISE, 25,
                    Team.Economy.PricingType.RESTAURANT, 10
                )))
                .sponsors(new HashMap<>() {{
                    put(Team.Economy.IncomePeriodicity.WEEKLY, null);
                    put(Team.Economy.IncomePeriodicity.ANNUAL, null);
                }})
                .billboardDeal(null)
                .build())
            .fans(Team.Fans.builder().build())
            .buildings(Team.Buildings.builder()
                .stadium(new Team.Buildings.Stadium())
                .freeSlots(25)
                .facilities(new HashMap<>(Map.of(
                    Team.Buildings.Facility.TRAINING_CENTER, new Team.Buildings.FacilityData(),
                    Team.Buildings.Facility.YOUTH_PITCH, new Team.Buildings.FacilityData(),
                    Team.Buildings.Facility.SPORTS_DOCTORS, new Team.Buildings.FacilityData(),
                    Team.Buildings.Facility.VIDEO_ROOM, new Team.Buildings.FacilityData(),
                    Team.Buildings.Facility.SCOUTS, new Team.Buildings.FacilityData()
                    )))
                .build())
            .leagueStats(new HashMap<>(Map.of(
            1, Team.LeagueStats.builder().tablePosition(12).points(0).build()
            )))
            .build();
        // TODO apart from saving the team aggregate/projection, we need to store a created_team_event. Then Team domain object should have a
        //  method like
        //  create(created_team_event) and based on the event it populates itself. Then you save it with the repo
        // TODO notification for the user to know that his team has been created
        teamWriteRepository.save(team);
        List<Team> s = teamReadRepository.findAll();

        log.info("CreateTeamUseCase size{}", s.toString());
        players.forEach(playerWriteRepository::save);
        teamCreationEventPublisher.publish(TeamCreationEvent.builder()
            .teamId(team.getId().value()).teamValue(players.stream()
                .map(Player::getEconomy)
                .map(Player.Economy::getSalary)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)).build());
    }
}
