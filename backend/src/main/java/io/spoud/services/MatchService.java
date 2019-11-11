package io.spoud.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.producer.ResultProducer;
import io.spoud.repositories.PlayerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatchService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private Random random;

    @Autowired
    private ResultProducer resultProducer;

    @Autowired
    private EventService eventService;

    public MatchEO createMatch(List<UUID> playersUuid) {
        if (playersUuid.size() < 4) {
            throw new IllegalArgumentException("To few players");
        }
        MatchEO match = createNewMatch(2, new HashSet<>(playerRepository.findByUuids(playersUuid)));
        eventService.newMatchEvent(match);
        return match;
    }

    private MatchEO createNewMatch(int retry, Set<PlayerEO> players) {
        ArrayList<PlayerEO> listCopy = new ArrayList<>(players);
        List<PlayerEO> activePlayers = new ArrayList<PlayerEO>();
        IntStream.range(0, 4).forEach(i -> activePlayers.add(listCopy.remove(random.nextInt(listCopy.size()))));


        if (retry > 0) {
            ArrayList<PlayerEO> activeSorted = new ArrayList<>(activePlayers);
            activeSorted.sort(Comparator.comparing(player -> player.getOffensePoints() + player.getDefensePoints()));
            UUID lowest = activeSorted.get(0).getUuid();
            boolean lowestPlayerBlue =
                    lowest.equals(activePlayers.get(0).getUuid()) || lowest.equals(activePlayers.get(1).getUuid());
            lowest = activeSorted.get(1).getUuid();
            boolean secondPlayerBlue =
                    lowest.equals(activePlayers.get(0).getUuid()) || lowest.equals(activePlayers.get(1).getUuid());
            if (lowestPlayerBlue == secondPlayerBlue) {
                return createNewMatch(retry - 1, players);
            }
        }
        return MatchEO.builder()
                .uuid(UUID.randomUUID())
                .playerBlueDefenseUuid(activePlayers.get(0).getUuid())
                .playerBlueOffenseUuid(activePlayers.get(1).getUuid())
                .playerRedDefenseUuid(activePlayers.get(2).getUuid())
                .playerRedOffenseUuid(activePlayers.get(3).getUuid())
                .build();
    }

    public MatchEO evaluate(MatchEO match) {
        resultProducer.add(match);
        return match;
    }
}
