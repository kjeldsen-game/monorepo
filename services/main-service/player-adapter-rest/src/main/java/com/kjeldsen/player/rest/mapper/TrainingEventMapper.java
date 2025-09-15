package com.kjeldsen.player.rest.mapper;

import com.kjeldsen.player.application.usecases.trainings.GetActiveScheduledTrainingsUseCase;
import com.kjeldsen.player.domain.models.training.TrainingEvent;
import com.kjeldsen.player.rest.mapper.common.EnumMapper;
import com.kjeldsen.player.rest.model.*;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(uses = {IdMapper.class})
public interface TrainingEventMapper {

    TrainingEventMapper INSTANCE = Mappers.getMapper(TrainingEventMapper.class);

//    default PlayerSkill fromPlayerSkillDomain(com.kjeldsen.player.domain.PlayerSkill playerSkillDomain) {
//        return PlayerSkill.valueOf(playerSkillDomain.name());
//    }

    default TrainingType fromDomainTrainingType(TrainingEvent.TrainingType domainTrainingType) {
        return EnumMapper.mapEnum(domainTrainingType, TrainingType.class);
    }

    default TrainingEvent.TrainingType fromTrainingType(TrainingType trainingType) {
        return EnumMapper.mapEnum(trainingType, TrainingEvent.TrainingType.class);
    }

    default TrainingModifier fromDomainTrainingModifier(TrainingEvent.TrainingModifier domainTrainingModifier) {
        return EnumMapper.mapEnum(domainTrainingModifier, TrainingModifier.class);
    }

    @Mapping(source = "type", target = "trainingType")
    TrainingEventResponse fromTrainingEvent(TrainingEvent trainingEvent);

    PlayerScheduledTrainingResponse mapPlayerScheduledTraining(GetActiveScheduledTrainingsUseCase.PlayerScheduledTraining playerScheduledTraining);

    default List<PlayerScheduledTrainingResponse> mapPlayerScheduledTrainingList(List<GetActiveScheduledTrainingsUseCase.PlayerScheduledTraining> domainList) {
        return domainList.stream().map(this::mapPlayerScheduledTraining).toList();
    }

    default Map<String, List<TrainingEventResponse>> fromTrainingEventsMap(
        Map<String, List<TrainingEvent>> eventsMap) {
        return eventsMap.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .map(this::fromTrainingEvent)
                    .toList()));
    }
}
