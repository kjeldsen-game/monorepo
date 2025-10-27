package com.kjeldsen.match.application.usecases.league.team;

import com.kjeldsen.match.domain.repositories.LeagueReadRepository;
import com.kjeldsen.match.domain.repositories.LeagueWriteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public abstract class AbstractAddTeamToLeagueUseCase {

    @Autowired
    protected   LeagueWriteRepository leagueWriteRepository;
    @Autowired
    protected  LeagueReadRepository leagueReadRepository;
}
