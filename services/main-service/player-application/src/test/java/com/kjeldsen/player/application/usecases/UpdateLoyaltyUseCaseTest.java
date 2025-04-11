package com.kjeldsen.player.application.usecases;

import com.kjeldsen.player.application.testdata.TestData;
import com.kjeldsen.player.application.usecases.fanbase.UpdateLoyaltyUseCase;
import com.kjeldsen.player.domain.Team;
import com.kjeldsen.player.domain.repositories.TeamWriteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UpdateLoyaltyUseCaseTest {

    private final GetTeamUseCase mockedGetTeamUseCase = Mockito.mock(GetTeamUseCase.class);
    private final TeamWriteRepository mockedTeamWriteRepository = Mockito.mock(TeamWriteRepository.class);
    private final UpdateLoyaltyUseCase updateLoyaltyUseCase = new UpdateLoyaltyUseCase(mockedGetTeamUseCase,
            mockedTeamWriteRepository);

    private static Team.TeamId mockedTeamId;

    @BeforeAll
    static void setUpBeforeClass() {
        mockedTeamId = TestData.generateTestTeamId();
    }

    @Test
    @DisplayName("Should reset all loyalty levels to 50")
    public void should_reset_all_loyalty_levels_to_50() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getFans().updateAllLoyaltyTiers(60.0);

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        updateLoyaltyUseCase.resetLoyalty(mockedTeamId);
        Map<Integer, Team.Fans.FanTier> testDataMap = mockedTeam.getFans().getFanTiers();
        assertThat(testDataMap.values())
            .allSatisfy(fanTier -> assertThat(fanTier.getLoyalty()).isEqualTo(50.0));
    }

    @Test
    @DisplayName("Should return new loyalty for MATCH_WIN/MATCH_LOSS impact")
    public void should_return_loyalty_for_MATCH_WIN_impact() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getFans().setFanTiers(new HashMap<>() {{
            put(1, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
            put(2, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
            put(3, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
            put(4, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
            put(5, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
        }});

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        updateLoyaltyUseCase.updateLoyaltyMatch(mockedTeamId, 5, Team.Fans.LoyaltyImpactType.MATCH_WIN);
        // Expected results WIN --> 6.0, 8.0, 9.0, 10.0, 12.0
        // Expected results Goals --> 1.0, 1.5, 2.0, 2.5, 3.0 --> multiplier 3 --> 3.0, 4.5, 6.0, 7.5, 9.0
        // Total + per Tier --> 9.0 , 12.5 , 15, 17.5, 21.0
        assertEquals(59.0, mockedTeam.getFans().getFanTiers().get(1).getLoyalty());
        assertEquals(62.5, mockedTeam.getFans().getFanTiers().get(2).getLoyalty());
        assertEquals(65.0, mockedTeam.getFans().getFanTiers().get(3).getLoyalty());
        assertEquals(67.5, mockedTeam.getFans().getFanTiers().get(4).getLoyalty());
        assertEquals(71.0, mockedTeam.getFans().getFanTiers().get(5).getLoyalty());
    }

    @Test
    @DisplayName("Should move the fans between tier MATCH impact")
    public void should_move_the_fans_between_ier() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getFans().setFanTiers(new HashMap<>() {{
            put(1, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
            put(2, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
        }});

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        updateLoyaltyUseCase.updateLoyaltyMatch(mockedTeamId, 5, Team.Fans.LoyaltyImpactType.MATCH_WIN);
        // Total + per Tier --> 9.0 , 12.5
        // Movement 0.9% and 1.25%
        assertEquals(99, mockedTeam.getFans().getFanTiers().get(1).getTotalFans());
        assertEquals(100, mockedTeam.getFans().getFanTiers().get(2).getTotalFans());
        assertEquals(1, mockedTeam.getFans().getFanTiers().get(3).getTotalFans());
    }

    @Test
    @DisplayName("Should return new loyalty for SEASON_END impact")
    public void should_return_loyalty_for_SEASON_END_impact() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getLeagueStats().put(10, Team.LeagueStats.builder().points(10).tablePosition(5).build());
        mockedTeam.getFans().setFanTiers(new HashMap<>() {{
            put(1, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
            put(2, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
            put(3, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
            put(4, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
            put(5, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
        }});

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        updateLoyaltyUseCase.updateLoyaltySeason(mockedTeamId);
        // Expected results +10 for 5th place
        assertEquals(60, mockedTeam.getFans().getFanTiers().get(1).getLoyalty());
        assertEquals(60, mockedTeam.getFans().getFanTiers().get(2).getLoyalty());
        assertEquals(60, mockedTeam.getFans().getFanTiers().get(3).getLoyalty());
        assertEquals(60, mockedTeam.getFans().getFanTiers().get(4).getLoyalty());
        assertEquals(60, mockedTeam.getFans().getFanTiers().get(5).getLoyalty());
    }

    @Test
    @DisplayName("Should return new loyalty for SEASON_END impact fans decrease")
    public void should_return_loyalty_for_SEASON_END_impact_fans_decrease() {
        Team mockedTeam = TestData.generateTestTeam(mockedTeamId);
        mockedTeam.getLeagueStats().put(10, Team.LeagueStats.builder().points(10).tablePosition(12).build());
        mockedTeam.getFans().setFanTiers(new HashMap<>() {{
            put(1, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
            put(2, Team.Fans.FanTier.builder().loyalty(50.0).totalFans(100).build());
        }});

        when(mockedGetTeamUseCase.get(mockedTeamId)).thenReturn(mockedTeam);

        updateLoyaltyUseCase.updateLoyaltySeason(mockedTeamId);
        // Expected results +10 for 5th place --> loyalty 40%

        assertEquals(100, mockedTeam.getFans().getFanTiers().get(1).getTotalFans());
        assertEquals(99, mockedTeam.getFans().getFanTiers().get(2).getTotalFans());
    }

}
