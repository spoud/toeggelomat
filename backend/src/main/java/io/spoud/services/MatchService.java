package io.spoud.services;

import io.spoud.entities.MatchEO;
import io.spoud.repositories.MatchRepository;
import io.spoud.repositories.PlayerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class MatchService {

  @Inject public PlayerRepository playerRepository;

  @Inject public MatchRepository matchRepository;

  @Inject public MatchRandomizeService matchRandomizeService;

  @Inject public MatchPointsService matchPointsService;

  public MatchEO randomizeMatch(List<UUID> playersUuid) {
    if (playersUuid.size() < 4) {
      throw new IllegalArgumentException("To few players");
    }
    MatchEO match =
        matchRandomizeService.randomizeNewMatch(
            2, new HashSet<>(playerRepository.findByIds(playersUuid)));
    match = matchPointsService.computePotentialPoints(match);
    return match;
  }

  public MatchEO saveMatchResults(MatchEO match) {
    MatchPointsService.PlayersHelper playerBefore =
        new MatchPointsService.PlayersHelper(playerRepository, match);

    match.matchTime = ZonedDateTime.now();
    match = matchPointsService.computePointsAndUpdatePlayers(match);
    matchRepository.persist(match);

    return match;
  }

  public List<MatchEO> getLastMatchOfTheSeason() {
    return matchRepository.getLastMatches(20);
  }
}
