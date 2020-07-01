package io.spoud.repositories;

import io.spoud.data.entities.MatchEO;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MatchRepository {

  public static Map<UUID, MatchEO> repo = new ConcurrentHashMap<>();

  public List<MatchEO> getLastMatches(int nb) {
    return repo.values().stream()
        .sorted(Comparator.comparing(MatchEO::getMatchTime))
        .limit(nb)
        .collect(Collectors.toList());
  }

  public MatchEO addMatch(MatchEO match) {
    if (match.getUuid() == null) {
      match.setUuid(UUID.randomUUID());
    }
    return repo.put(match.getUuid(), match);
  }
}
