package io.spoud.rest;

import io.spoud.entities.MatchEO;
import io.spoud.services.MatchMakingService;
import java.util.Collection;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchResource {
  @Autowired
  private MatchMakingService matchMakingService;

  @PostMapping(path="/finish")
  public MatchEO finishMatch(MatchEO match) {
    return matchMakingService.evaluate(match);
  }

  @PostMapping()
  public MatchEO startMatchWithPlayers(Collection<UUID> players) {
    return matchMakingService.createMatch(players);
  }
}
