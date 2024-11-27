package com.kjeldsen.player.application.usecases.player;

import com.kjeldsen.player.domain.Player;
import com.kjeldsen.player.domain.PlayerAge;
import com.kjeldsen.player.domain.repositories.PlayerReadRepository;
import com.kjeldsen.player.domain.repositories.PlayerWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlayerAgingUseCase {

    private final PlayerReadRepository playerReadRepository;
    private final PlayerWriteRepository playerWriteRepository;

    public void increaseAge() {
        List<Player> players = playerReadRepository.findAll();
        log.info("PlayerAgingUseCase executed for total {} players!", players.size());
        players.forEach(player -> {
            playerAging(player);
            playerWriteRepository.save(player);
        });
    }

    public void playerAging(Player player){
        PlayerAge age = player.getAge();
        log.info("before executed for age {} {} {}", age.getYears(), age.getMonths(), age.getDays());
        PlayerAge playerAge = PlayerAge.gettingOlder(age);
        log.info("after executed for age {} {} {}", playerAge.getYears(), playerAge.getMonths(), playerAge.getDays());
        player.setAge(playerAge);
    }
}
