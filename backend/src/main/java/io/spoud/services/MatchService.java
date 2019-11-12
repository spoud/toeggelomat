package io.spoud.services;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.spoud.entities.MatchEO;
import io.spoud.producer.ResultProducer;
import io.spoud.repositories.MatchRepository;
import io.spoud.repositories.PlayerRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatchService {

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  private ResultProducer resultProducer;

  @Autowired
  private EventService eventService;

  @Autowired
  private MatchRepository matchRepository;

  @Autowired
  private MatchRandomizeService matchRandomizeService;

  public MatchEO createMatch(List<UUID> playersUuid) {
    if (playersUuid.size() < 4) {
      throw new IllegalArgumentException("To few players");
    }
    MatchEO match = matchRandomizeService.randomizeNewMatch(2,
      new HashSet<>(playerRepository.findByUuids(playersUuid)));
    eventService.newMatchEvent(match);
    return match;
  }

  public MatchEO evaluate(MatchEO match) {
    matchRepository.addMatch(match);
    // TODO compute point
    resultProducer.add(match);
    return match;
  }
}
