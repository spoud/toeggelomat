package io.spoud.services;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

@ApplicationScoped
public class MatchRandomizeService {

  @Inject public Random random;

  public MatchEO randomizeNewMatch(int retry, Set<PlayerEO> players) {
    ArrayList<PlayerEO> listCopy = new ArrayList<>(players);
    List<PlayerEO> activePlayers = new ArrayList<PlayerEO>();
    IntStream.range(0, 4)
        .forEach(i -> activePlayers.add(listCopy.remove(random.nextInt(listCopy.size()))));

    if (retry > 0) {
      ArrayList<PlayerEO> activeSorted = new ArrayList<>(activePlayers);
      activeSorted.sort(
          Comparator.comparing(player -> player.offensePoints + player.defensePoints));
      UUID lowest = activeSorted.get(0).uuid;
      boolean lowestPlayerBlue =
          lowest.equals(activePlayers.get(0).uuid) || lowest.equals(activePlayers.get(1).uuid);
      lowest = activeSorted.get(1).uuid;
      boolean secondPlayerBlue =
          lowest.equals(activePlayers.get(0).uuid) || lowest.equals(activePlayers.get(1).uuid);
      if (lowestPlayerBlue == secondPlayerBlue) {
        return randomizeNewMatch(retry - 1, players);
      }
    }
    return MatchEO.newMatch(
        activePlayers.get(0).uuid,
        activePlayers.get(1).uuid,
        activePlayers.get(2).uuid,
        activePlayers.get(3).uuid);
  }
}
