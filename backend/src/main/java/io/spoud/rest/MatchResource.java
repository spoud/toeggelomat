package io.spoud.rest;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.spoud.entities.MatchEO;
import io.spoud.services.MatchService;

@ApplicationScoped
@Path("/api/v1/matches")
public class MatchResource {
  @Inject
  private MatchService matchService;

  @POST
  @Path("set-score")
  public MatchEO finishMatch(MatchEO match) {
    return matchService.saveMatchResults(match);
  }

  @POST
  @Path("randomize")
  public MatchEO startMatchWithPlayers(List<UUID> players) {
    return matchService.randomizeMatch(players);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<MatchEO> findAll() {
    return matchService.getLastMatchOfTheSeason();
  }
}
