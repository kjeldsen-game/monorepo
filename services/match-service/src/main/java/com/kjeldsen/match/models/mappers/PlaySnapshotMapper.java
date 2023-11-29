package com.kjeldsen.match.models.mappers;

import com.kjeldsen.match.engine.entities.Play;
import com.kjeldsen.match.models.snapshot.PlaySnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",  uses = {PlayerSnapshotMapper.class})
public interface PlaySnapshotMapper {

    @Mapping(source = "duel.type", target = "duelType")
    @Mapping(source = "duel.initiator", target = "initiator")
    @Mapping(source = "duel.challenger", target = "challenger")
    @Mapping(source = "duel.receiver", target = "receiver")
    @Mapping(source = "duel.pitchArea", target = "pitchArea")
    @Mapping(source = "duel.result", target = "result")
    @Mapping(source = "duel.initiatorStats", target = "initiatorStats")
    @Mapping(source = "duel.challengerStats", target = "challengerStats")
    @Mapping(source = "duel.origin", target = "origin")
    PlaySnapshot toSnapshot(Play play);
}
