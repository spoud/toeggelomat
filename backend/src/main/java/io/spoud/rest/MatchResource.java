package io.spoud.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.spoud.entities.MatchEO;
import io.spoud.services.MatchService;

@RestController
@RequestMapping("/api/v1/matches")
public class MatchResource {
    @Autowired
    private MatchService matchMakingService;

    @PostMapping(path = "/finish")
    public MatchEO finishMatch(MatchEO match) {
        return matchMakingService.evaluate(match);
    }

    @PostMapping()
    public MatchEO startMatchWithPlayers(List<UUID> players) {
        return matchMakingService.createMatch(players);
    }
}
