package io.spoud.repositories;

import io.spoud.data.kafka.MatchResultBO;

import javax.enterprise.context.ApplicationScoped;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class MatchRepository {

  public static Map<UUID, MatchResultBO> repo = new ConcurrentHashMap<>();

  public List<MatchResultBO> getLastMatches(int nb) {
    return repo.values().stream()
        .sorted(Comparator.comparing(MatchResultBO::getMatchTime))
        .limit(nb)
        .collect(Collectors.toList());
  }

  public MatchResultBO addMatch(MatchResultBO match) {
    if (match.getUuid() == null) {
      match.setUuid(UUID.randomUUID());
    }
    return repo.put(match.getUuid(), match);
  }
}
