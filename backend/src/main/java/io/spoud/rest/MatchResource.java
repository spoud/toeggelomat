package io.spoud.rest;

import io.spoud.data.MatchPropositionBO;
import io.spoud.data.MatchResultWithPointsBO;
import io.spoud.services.MatchService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
