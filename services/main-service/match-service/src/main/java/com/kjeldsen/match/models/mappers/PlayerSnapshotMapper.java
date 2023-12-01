package com.kjeldsen.match.models.mappers;

import com.kjeldsen.match.models.Player;
import com.kjeldsen.match.models.snapshot.PlayerSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlayerSnapshotMapper {

    @Mapping(ignore = true, target = "team")
    PlayerSnapshot toSnapshot(Player player);
}
