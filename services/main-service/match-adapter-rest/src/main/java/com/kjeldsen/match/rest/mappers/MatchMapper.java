package com.kjeldsen.match.rest.mappers;

import com.kjeldsen.match.domain.entities.Match;
import com.kjeldsen.match.domain.entities.MatchReport;
import com.kjeldsen.match.rest.model.MatchReportResponse;
import com.kjeldsen.match.rest.model.MatchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {IdMapper.class, TeamMapper.class})
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchResponse map(Match match);

    MatchReportResponse map(MatchReport matchReport);

}
