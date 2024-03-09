package io.spoud.rest;

import io.spoud.data.MatchPropositionBO;
import io.spoud.data.MatchResultWithPointsBO;
import io.spoud.services.MatchService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Path("/api/v1/matches")
public class MatchResource {
  @Inject private MatchService matchService;

  @POST
  @Path("set-score")
  public MatchPropositionBO finishMatch(MatchPropositionBO match) {
    return matchService.saveMatchResults(match);
  }

  @POST
  @Path("randomize")
  public MatchPropositionBO startMatchWithPlayers(List<UUID> players) {
    return matchService.randomizeMatch(players);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<MatchResultWithPointsBO> findAll() {
    return matchService.getLastMatchOfTheSeason();
  }

  @GET
  @Path("add")
  @Produces(MediaType.APPLICATION_JSON)
  public MatchPropositionBO addRandom() {
    return matchService.addSome();
  }
}
