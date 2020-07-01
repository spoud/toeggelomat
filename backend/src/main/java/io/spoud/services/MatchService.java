package io.spoud.services;

import io.spoud.data.entities.MatchEO;
import io.spoud.data.kafka.MatchResultBO;
import io.spoud.data.kafka.Player;
import io.spoud.producer.ResultProducer;
import io.spoud.repositories.MatchRepository;
import io.spoud.repositories.PlayerRepository;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Transactional
@Slf4j
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

    match.setMatchTime(ZonedDateTime.now());
    //matchRepository.addMatch(match);

    resultProducer.add(
        MatchResultBO.builder()
            .uuid(match.getUuid())
            .redScore(match.getRedScore())
            .blueScore(match.getBlueScore())
            .matchTime(match.getMatchTime())
            .blueDefense(match.getBlueDefense())
            .blueOffense(match.getBlueOffense())
            .redDefense(match.getRedDefense())
            .redOffense(match.getRedOffense())
            .build());
    eventService.scoreChangedEvent();
    return match;
  }

  public MatchEO addSome() {
    var players =
        playerRepository.getAllPlayers().stream()
            .map(Player::getUuid)
            .collect(Collectors.toList());

    var match = this.randomizeMatch(players);
    match.setBlueScore(7);
    match.setRedScore(2);
    saveMatchResults(match);

    return match;
  }

  public List<MatchEO> getLastMatchOfTheSeason() {
    return matchRepository.getLastMatches(20);
  }
}
