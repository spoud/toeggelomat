package io.spoud.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatchRandomizeService {

  @Autowired
  private Random random;

  public MatchEO randomizeNewMatch(int retry, Set<PlayerEO> players) {
    ArrayList<PlayerEO> listCopy = new ArrayList<>(players);
    List<PlayerEO> activePlayers = new ArrayList<PlayerEO>();
    IntStream.range(0, 4)
      .forEach(i -> activePlayers.add(listCopy.remove(random.nextInt(listCopy.size()))));

    if (retry > 0) {
      ArrayList<PlayerEO> activeSorted = new ArrayList<>(activePlayers);
      activeSorted.sort(
        Comparator.comparing(player -> player.getOffensePoints() + player.getDefensePoints()));
      UUID lowest = activeSorted.get(0).getUuid();
      boolean lowestPlayerBlue = lowest.equals(activePlayers.get(0).getUuid()) || lowest.equals(
        activePlayers.get(1).getUuid());
      lowest = activeSorted.get(1).getUuid();
      boolean secondPlayerBlue = lowest.equals(activePlayers.get(0).getUuid()) || lowest.equals(
        activePlayers.get(1).getUuid());
      if (lowestPlayerBlue == secondPlayerBlue) {
        return randomizeNewMatch(retry - 1, players);
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
}