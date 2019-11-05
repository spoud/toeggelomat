package io.spoud.rest;

import io.spoud.entities.MatchEO;
import io.spoud.services.MatchMakingService;
import java.util.Collection;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchResource {
  @Autowired
  private MatchMakingService matchMakingService;



  @PostMapping(path="/finish",params = {"match"})
  public MatchEO finishMatch(@RequestParam("match")MatchEO match) {
    return matchMakingService.evaluate(match);
  }

  @PostMapping(params = {"players"})
  public MatchEO startMatchWithPlayers(@RequestParam("players")Collection<UUID> uuids) {
    return matchMakingService.createMatch(uuids);
  }
}
