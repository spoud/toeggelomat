package io.spoud.services;

import io.spoud.entities.MatchEO;
import io.spoud.producer.MatchResultKafkaBO;
import io.spoud.producer.ResultProducer;
import io.spoud.repositories.MatchRepository;
import io.spoud.repositories.PlayerRepository;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class MatchService {

  @Inject private PlayerRepository playerRepository;

  @Inject private ResultProducer resultProducer;

  @Inject private EventService eventService;

  @Inject private MatchRepository matchRepository;

  @Inject private MatchRandomizeService matchRandomizeService;

  @Inject private MatchPointsService matchPointsService;

  public MatchEO randomizeMatch(List<UUID> playersUuid) {
    if (playersUuid.size() < 4) {
      throw new IllegalArgumentException("To few players");
    }
    MatchEO match =
        matchRandomizeService.randomizeNewMatch(
            2, new HashSet<>(playerRepository.findByUuids(playersUuid)));
    match = matchPointsService.computePotentialPoints(match);
    eventService.newMatchEvent(match);
    return match;
  }

  public MatchEO saveMatchResults(MatchEO match) {
    MatchPointsService.PlayersHelper playerBefore =
        new MatchPointsService.PlayersHelper(playerRepository, match);

    MatchResultKafkaBO.MatchResultKafkaBOBuilder matchResultKafkaBOBuilder =
        MatchResultKafkaBO.builder()
            .blueDeffenseBefore(playerBefore.getBlueDeffense().clone())
            .blueOffenseBefore(playerBefore.getBlueOffense().clone())
            .redDeffenseBefore(playerBefore.getRedDeffense().clone())
            .redOffenseBefore(playerBefore.getRedOffense().clone());

    match.setMatchTime(ZonedDateTime.now());
    match = matchPointsService.computePointsAndUpdatePlayers(match);
    matchRepository.addMatch(match);

    MatchPointsService.PlayersHelper playerAfter =
        new MatchPointsService.PlayersHelper(playerRepository, match);
    matchResultKafkaBOBuilder
        .matchUuid(match.getUuid())
        .redScore(match.getRedScore())
        .blueScore(match.getBlueScore())
        .points(match.getPoints())
        .matchTime(match.getMatchTime())
        .blueDeffenseAfter(playerAfter.getBlueDeffense())
        .blueOffenseAfter(playerAfter.getBlueOffense())
        .redDeffenseAfter(playerAfter.getRedDeffense())
        .redOffenseAfter(playerAfter.getRedOffense());

    resultProducer.add(matchResultKafkaBOBuilder.build());
    eventService.scoreChangedEvent();
    return match;
  }

  public List<MatchEO> getLastMatchOfTheSeason() {
    return matchRepository.getLastMatches(20);
  }
}
