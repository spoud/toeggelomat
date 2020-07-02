package io.spoud.rest;

import io.spoud.data.entities.MatchProposition;
import io.spoud.data.kafka.MatchResultBO;
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
  public MatchProposition finishMatch(MatchProposition match) {
    return matchService.saveMatchResults(match);
  }

  @POST
  @Path("randomize")
  public MatchProposition startMatchWithPlayers(List<UUID> players) {
    return matchService.randomizeMatch(players);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<MatchResultBO> findAll() {
    return matchService.getLastMatchOfTheSeason();
  }

  @GET
  @Path("add")
  @Produces(MediaType.APPLICATION_JSON)
  public MatchProposition addRandom() {
    return matchService.addSome();
  }

}
