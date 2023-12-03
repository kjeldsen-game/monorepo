package com.kjeldsen.match.rest.delegate;

import com.kjeldsen.match.rest.api.MatchApiDelegate;
import com.kjeldsen.match.rest.model.CreateMatchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MatchDelegate implements MatchApiDelegate {

    @Override
    public ResponseEntity<Void> createMatch(CreateMatchRequest createMatchRequest) {
//        CreateMatchUseCase.NewMatch newMatch = CreateMatchMapper.INSTANCE.map(createMatchRequest);
//        newMatch.setTeamId(Team.TeamId.of("NOTEAM")); // TODO change to receive team id in api?
//        createMatchUseCase.create(newMatch);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
