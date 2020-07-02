package io.spoud.services;

import io.spoud.data.MatchPropositionBO;
import io.spoud.data.MatchResultBO;
import io.spoud.data.MatchResultWithPointsBO;
import io.spoud.data.PlayerBO;
import io.spoud.repositories.MatchRepository;
import io.spoud.repositories.PlayerRepository;
import io.spoud.streams.producer.ResultProducer;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
@Transactional
@Slf4j
public class MatchService {

  @Inject Random random;

  @Inject PlayerRepository playerRepository;

  @Inject ResultProducer resultProducer;

  @Inject EventService eventService;

  @Inject MatchRepository matchRepository;

  @Inject MatchRandomizeService matchRandomizeService;

  @Inject MatchPointsService matchPointsService;

  public MatchPropositionBO randomizeMatch(List<UUID> playersUuid) {
    if (playersUuid.size() < 4) {
      throw new IllegalArgumentException("To few players");
    }
    MatchPropositionBO match =
        matchRandomizeService.randomizeNewMatch(
            2, new HashSet<>(playerRepository.findByUuids(playersUuid)));
    match = matchPointsService.computePotentialPoints(match);
    eventService.newMatchEvent(match);
    return match;
  }

  public MatchPropositionBO saveMatchResults(MatchPropositionBO match) {
    match.setMatchTime(ZonedDateTime.now());
    resultProducer.add(
        MatchResultBO.builder()
            .uuid(match.getUuid())
            .redScore(match.getRedScore())
            .blueScore(match.getBlueScore())
            .matchTime(match.getMatchTime())
            .playerBlueDefenseUuid(match.getPlayerBlueDefenseUuid())
            .playerBlueOffenseUuid(match.getPlayerBlueOffenseUuid())
            .playerRedDefenseUuid(match.getPlayerRedDefenseUuid())
            .playerRedOffenseUuid(match.getPlayerRedOffenseUuid())
            .build());
    return match;
  }

  public MatchPropositionBO addSome() {
    var players =
        playerRepository.getAllPlayers().stream()
            .map(PlayerBO::getUuid)
            .collect(Collectors.toList());

    var match = this.randomizeMatch(players);
    match.setBlueScore(7);
    match.setRedScore(random.nextInt(7));
    saveMatchResults(match);

    return match;
  }

  public List<MatchResultWithPointsBO> getLastMatchOfTheSeason() {
    return matchRepository.getLastMatches(20);
  }
}
