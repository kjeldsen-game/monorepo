package com.kjeldsen.match.models.mappers;

import com.kjeldsen.match.models.Team;
import com.kjeldsen.match.models.snapshot.TeamSnapshot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PlayerSnapshotMapper.class})
public interface TeamSnapshotMapper {

    TeamSnapshot toSnapshot(Team team);
}
