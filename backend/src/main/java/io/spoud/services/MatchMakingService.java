package io.spoud.services;

import io.spoud.entities.CurrentMatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MatchMakingService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private Random random = new SecureRandom();

    private Set<PlayerEO> queue = new HashSet<>();

    public void addPlayerToQueue(UUID playerUuid) {
        PlayerEO player = playerRepository.findByUuid(playerUuid).orElseThrow(() -> new NotFoundException("User with uuid '" + playerUuid + "' not found"));
        queue.add(player);
    }

    public void createMatch(Set<UUID> players) {
        if (players.size() < 4) {
            throw new IllegalArgumentException("To few players");
        }
        players.forEach(this::addPlayerToQueue);
        createNewMatch(2);
    }

    private CurrentMatchEO createNewMatch(int retry) {
        ArrayList<PlayerEO> listCopy = new ArrayList<>(queue);
        CurrentMatchEO match = CurrentMatchEO.builder()
            .playerBlueDefense(listCopy.remove(random.nextInt(listCopy.size())))
            .playerBlueOffense(listCopy.remove(random.nextInt(listCopy.size())))
            .playerRedDefense(listCopy.remove(random.nextInt(listCopy.size())))
            .playerRedOffense(listCopy.remove(random.nextInt(listCopy.size()))).build();

        if (retry > 0) {
            var activePlayers = new ArrayList<PlayerEO>();
            activePlayers.add(match.getPlayerBlueDefense());
            activePlayers.add(match.getPlayerBlueOffense());
            activePlayers.add(match.getPlayerRedDefense());
            activePlayers.add(match.getPlayerRedOffense());
            activePlayers.sort(Comparator.comparing(PlayerEO::getPoints));
            var lowest = activePlayers.get(0).getUuid();
            boolean lowestPlayerBlue =
                lowest.equals(match.getPlayerBlueDefense().getUuid()) || lowest
                    .equals(match.getPlayerBlueOffense().getUuid());
            lowest = activePlayers.get(1).getUuid();
            boolean secondPlayerBlue =
                lowest.equals(match.getPlayerBlueDefense().getUuid()) || lowest
                    .equals(match.getPlayerBlueOffense().getUuid());
            if(lowestPlayerBlue==secondPlayerBlue){
                createNewMatch(retry-1);
            }
        }
        return match;
    }
}
