package com.kjeldsen.match.controllers;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kjeldsen.match.engine.RandomHelper;
import com.kjeldsen.match.models.Match;
import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.repositories.TeamRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MatchControllerTest {

    private TeamRepository teamRepository;
    private MatchController controller;

    @BeforeEach
    void setUp() {
        teamRepository = mock(TeamRepository.class);
        controller = new MatchController(teamRepository);
    }

    @Test
    void homeTeamLookupUsesHomeId() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        when(teamRepository.findById(home.getId())).thenReturn(Optional.of(home));
        when(teamRepository.findById(away.getId())).thenReturn(Optional.of(away));

        try {
            controller.create(Match.builder().home(home).away(away).build());
        } catch (ValidationException e) {
            throw e;
        } catch (Exception ignored) {
            // Game engine may throw unrelated exceptions; we only verify the lookup arg
        }

        verify(teamRepository).findById(home.getId());
    }

    @Test
    void awayTeamLookupUsesAwayId() {
        Team home = RandomHelper.genTeam();
        Team away = RandomHelper.genTeam();
        when(teamRepository.findById(home.getId())).thenReturn(Optional.of(home));
        when(teamRepository.findById(away.getId())).thenReturn(Optional.of(away));

        try {
            controller.create(Match.builder().home(home).away(away).build());
        } catch (ValidationException e) {
            throw e;
        } catch (Exception ignored) {
            // Game engine may throw unrelated exceptions; we only verify the lookup arg
        }

        verify(teamRepository).findById(away.getId());
    }

    @Test
    void throwsWhenHomeTeamNotFound() {
        Team home = Team.builder().id(1L).build();
        Team away = Team.builder().id(2L).build();
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
            () -> controller.create(Match.builder().home(home).away(away).build()));
    }

    @Test
    void throwsWhenAwayTeamNotFound() {
        Team home = Team.builder().id(1L).build();
        Team away = Team.builder().id(2L).build();
        when(teamRepository.findById(1L)).thenReturn(Optional.of(home));
        when(teamRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
            () -> controller.create(Match.builder().home(home).away(away).build()));
    }
}
