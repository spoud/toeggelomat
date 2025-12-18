package io.spoud.services;

import io.spoud.api.data.SaveScoreInput;
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

  @Inject PlayerRepository playerRepository;

  @Inject MatchRepository matchRepository;

  @Inject MatchRandomizeService matchRandomizeService;

  @Inject MatchPointsService matchPointsService;

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

  public MatchEO saveMatchResults(SaveScoreInput score) {
    MatchEO match = new MatchEO();
    match.uuid = UUID.randomUUID();
    match.matchTime = ZonedDateTime.now();
    match.redScore = score.redScore();
    match.blueScore = score.blueScore();
    match.playerRedDefenseUuid =score.playerRedDefenseUuid();
    match.playerRedOffenseUuid = score.playerRedOffenseUuid();
    match.playerBlueDefenseUuid = score.playerBlueDefenseUuid();
    match.playerBlueOffenseUuid = score.playerBlueOffenseUuid();

    match = matchPointsService.computePointsAndUpdatePlayers(match);
    matchRepository.persistAndFlush(match);

    return match;
  }

  public List<MatchEO> getLastMatchOfTheSeason() {
    return matchRepository.getLastMatches(20);
  }
}
