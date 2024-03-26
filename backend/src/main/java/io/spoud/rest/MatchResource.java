package io.spoud.rest;

import io.spoud.entities.MatchEO;
import io.spoud.services.MatchService;
import java.util.List;
import java.util.UUID;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/api/v1/matches")
public class MatchResource {
  @Inject MatchService matchService;

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
