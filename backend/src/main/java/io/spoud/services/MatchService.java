package io.spoud.services;

import io.spoud.entities.MatchEO;
import io.spoud.repositories.MatchRepository;
import io.spoud.repositories.PlayerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class MatchService {

  private final PlayerRepository playerRepository;

  private final MatchRepository matchRepository;

  private final MatchRandomizeService matchRandomizeService;

  private final MatchPointsService matchPointsService;

  public MatchEO randomizeMatch(List<UUID> playersUuid) {
    if (playersUuid.size() < 4) {
      throw new IllegalArgumentException("To few players");
    }
    MatchEO match =
        matchRandomizeService.randomizeNewMatch(
            2, new HashSet<>(playerRepository.findByUuids(playersUuid)));
    match = matchPointsService.computePotentialPoints(match);
    return match;
  }

  public MatchEO saveMatchResults(MatchEO match) {
    MatchPointsService.PlayersHelper playerBefore =
        new MatchPointsService.PlayersHelper(playerRepository, match);

    match.setMatchTime(ZonedDateTime.now());
    match = matchPointsService.computePointsAndUpdatePlayers(match);
    matchRepository.addMatch(match);

    return match;
  }

  public List<MatchEO> getLastMatchOfTheSeason() {
    return matchRepository.getLastMatches(20);
  }
}
