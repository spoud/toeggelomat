package io.spoud.rest;

import io.spoud.entities.MatchEO;
import io.spoud.services.MatchService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Path("/api/v1/matches")
@RequiredArgsConstructor
public class MatchResource {
  private final MatchService matchService;

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
