package io.spoud.services;

import io.spoud.data.entities.MatchEO;
import io.spoud.data.kafka.Player;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class MatchRandomizeService {

  @Inject private Random random;

  public MatchEO randomizeNewMatch(int retry, Set<Player> players) {
    ArrayList<Player> listCopy = new ArrayList<>(players);
    List<Player> activePlayers = new ArrayList<Player>();
    IntStream.range(0, 4)
        .forEach(i -> activePlayers.add(listCopy.remove(random.nextInt(listCopy.size()))));

    if (retry > 0) {
      ArrayList<Player> activeSorted = new ArrayList<>(activePlayers);
      activeSorted.sort(
          Comparator.comparing(player -> player.getOffensePoints() + player.getDefensePoints()));
      UUID lowest = activeSorted.get(0).getUuid();
      boolean lowestPlayerBlue =
          lowest.equals(activePlayers.get(0).getUuid())
              || lowest.equals(activePlayers.get(1).getUuid());
      lowest = activeSorted.get(1).getUuid();
      boolean secondPlayerBlue =
          lowest.equals(activePlayers.get(0).getUuid())
              || lowest.equals(activePlayers.get(1).getUuid());
      if (lowestPlayerBlue == secondPlayerBlue) {
        return randomizeNewMatch(retry - 1, players);
      }
    }
    return MatchEO.builder()
        .uuid(UUID.randomUUID())
        .blueDefense(activePlayers.get(0).getUuid())
        .blueOffense(activePlayers.get(1).getUuid())
        .redDefense(activePlayers.get(2).getUuid())
        .redOffense(activePlayers.get(3).getUuid())
        .build();
  }
}
