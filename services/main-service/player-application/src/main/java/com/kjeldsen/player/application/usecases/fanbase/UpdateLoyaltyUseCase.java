package com.kjeldsen.player.application.usecases.fanbase;

import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamReadRepository;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateLoyaltyUseCase {

    private static final Double LOYALTY_MID = 50.0;
    private static final Integer MAX_TIER = 5;
    private static final Integer MIN_TIER = 1;
    private final TeamReadRepository teamReadRepository;
    private final TeamWriteRepository teamWriteRepository;

    public void updateLoyaltyMatch(Team.TeamId teamId, Integer goals, Team.Fans.LoyaltyImpactType loyaltyImpactType) {
        log.info("UpdateLoyaltyUseCase after match for team {}", teamId);
        Team team = getTeam(teamId);

        List<Double> calculatedLoyaltyPerTier = IntStream.range(0, getLoyaltyGoals(goals, loyaltyImpactType).size())
            .mapToObj(i -> getLoyaltyGoals(goals, loyaltyImpactType).get(i) + getLoyaltyMatch(loyaltyImpactType).get(i))
            .toList();

        team.getFans().updateAllLoyaltyTiers(calculatedLoyaltyPerTier);
        processFansTierMovement(team);
        teamWriteRepository.save(team);
    }

    public void updateLoyaltySeason(Team.TeamId teamId) {
        log.info("UpdateLoyaltyUseCase after season for team {}", teamId);

        Team team = getTeam(teamId);

        // Calculated Loyalty based on the position in current (previous) season
        Integer highestKey = team.getLeagueStats().keySet().stream()
            .max(Integer::compareTo)
            .orElseThrow(() -> new RuntimeException("Map is empty or contains no valid keys"));

        Double calculatedLoyalty = getLoyaltyNewSeason(team.getLeagueStats().get(highestKey).getTablePosition());

        team.getFans().updateAllLoyaltyTiers(calculatedLoyalty);
        processFansTierMovement(team);
        teamWriteRepository.save(team);
    }

    public void resetLoyalty(Team.TeamId teamId) {
        log.info("UpdateLoyaltyUseCase reset for team {}", teamId);

        Team team = getTeam(teamId);
        team.getFans().resetLoyalty();
        teamWriteRepository.save(team);
    }

    private Team getTeam(Team.TeamId teamId) {
        log.info("UpdateLoyaltyUseCase for team {}", teamId);
        return teamReadRepository.findById(teamId).orElseThrow(
            () -> new RuntimeException("Team not found"));
    }

    // TODO move this outside of this usecase -> 2 options refactor FansManagementUseCase or create fansTierMovementUseCase
    private void processFansTierMovement(Team team) {
        Map<Integer, Integer> fansTierMovement = new HashMap<>();
        for (Integer tier : team.getFans().getFanTiers().keySet()) {
            Team.Fans.FanTier fanTier = team.getFans().getFanTiers().get(tier);
            double loyaltyDifference = fanTier.getLoyalty() - LOYALTY_MID;
            fansTierMovement.put(tier, (int) Math.round(fanTier.getTotalFans() / 100 * (loyaltyDifference * 0.1)));
        }

        fansTierMovement.forEach((tier, numberOfFans) -> {
            log.info("Moving this amount of fans {} in tier {}", numberOfFans, tier);
            if (numberOfFans > 0) {
                if ((tier + 1) <= MAX_TIER) {
                    team.getFans().updateFans(tier, -numberOfFans); // Remove fans from current tier
                    team.getFans().updateFans(tier + 1, numberOfFans); // Add fans to the next tier
                }
            } else {
                if ((tier - 1) >= MIN_TIER) {
                    team.getFans().updateFans(tier, numberOfFans); // Subtract fans from higher Tier
                    team.getFans().updateFans(tier - 1, -numberOfFans); // Move fans to the lower Tier
                } else {
                    team.getFans().updateFans(1, numberOfFans); // If already in Tier 1, lose fans
                }
            }
        });
    }

    private List<Double> getLoyaltyMatch(Team.Fans.LoyaltyImpactType impactType) {
        return switch (impactType) {
            case MATCH_WIN -> List.of(6.0, 8.0, 9.0, 10.0, 12.0);
            case MATCH_LOSS -> List.of(-12.0, -10.0, -9.0, -8.0, -6.0);
            default -> throw new RuntimeException("Invalid impactType");
        };
    };

    private List<Double> getLoyaltyGoals(Integer goals, Team.Fans.LoyaltyImpactType impactType) {
        Integer multiplier = goals >= 3 ? goals - 2 : 0;
        List<Double> baseGoals = switch (impactType) {
            case MATCH_WIN -> List.of(1.0, 1.5, 2.0, 2.5, 3.0);
            case MATCH_LOSS -> List.of(-3.0, -2.5, -2.0, -1.5, -1.0);
            default -> throw new RuntimeException("Invalid impact type");
        };
        return baseGoals.stream().map(goal -> goal * multiplier).collect(Collectors.toList());
    }

    private Double getLoyaltyNewSeason(Integer prevSeasonPosition) {
        return switch (prevSeasonPosition) {
            case 1 -> 20.0;
            case 2,3 -> 15.0;
            case 4,5,6 -> 10.0;
            case 7,8 -> 5.0;
            case 9,10 -> -5.0;
            case 11,12 -> -10.0;
            default -> throw new RuntimeException("Invalid previous season position");
        };
    }

}
