package io.spoud.services;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
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
    private Random random;

    private Set<PlayerEO> queue = new HashSet<>();

    public void addPlayerToQueue(UUID playerUuid) {
        PlayerEO player = playerRepository.findByUuid(playerUuid).orElseThrow(() -> new NotFoundException("User with uuid '" + playerUuid + "' not found"));
        queue.add(player);
    }

    public MatchEO createMatch(Collection<UUID> players) {
        if (players.size() < 4) {
            throw new IllegalArgumentException("To few players");
        }
        players.forEach(this::addPlayerToQueue);
        return createNewMatch(2);
    }

    public MatchEO createNewMatch(int retry) {
        ArrayList<PlayerEO> listCopy = new ArrayList<>(queue);
        var activePlayers = new ArrayList<PlayerEO>();
        IntStream.range(0, 4)
            .forEach(i -> activePlayers.add(listCopy.remove(random.nextInt(listCopy.size()))));


        if (retry > 0) {
            ArrayList<PlayerEO> activeSorted = new ArrayList<>(activePlayers);
            activeSorted.sort(Comparator.comparing(PlayerEO::getPoints));
            var lowest = activeSorted.get(0).getUuid();
            boolean lowestPlayerBlue =
                lowest.equals(activePlayers.get(0).getUuid()) || lowest
                    .equals(activePlayers.get(1).getUuid());
            lowest = activeSorted.get(1).getUuid();
            boolean secondPlayerBlue =
                lowest.equals(activePlayers.get(0).getUuid()) || lowest
                    .equals(activePlayers.get(1).getUuid());
            if(lowestPlayerBlue==secondPlayerBlue){
                return createNewMatch(retry-1);
            }
        }
        return MatchEO.builder()
            .playerBlueDefenseUuid(activePlayers.get(0).getUuid())
            .playerBlueOffenseUuid(activePlayers.get(1).getUuid())
            .playerRedDefenseUuid(activePlayers.get(2).getUuid())
            .playerRedOffenseUuid(activePlayers.get(3).getUuid())
            .build();
    }


    public MatchEO evaluate(MatchEO match){

        return match;
    }
}
