package io.spoud.rest;

import io.spoud.data.MatchPropositionBO;
import io.spoud.services.EventService;
import io.spoud.services.MatchService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/api/v1/debug")
public class DebugResource {

  @Inject EventService eventService;

  @Inject MatchService matchService;

  @GET
  @Path("random-match")
  @Produces(MediaType.APPLICATION_JSON)
  public MatchPropositionBO addRandom() {
    return matchService.addSome();
  }

  @GET
  @Path("trigger-sse")
  @Produces(MediaType.APPLICATION_JSON)
  public String triggerSse() {
    eventService.scoreChangedEvent();
    return "done";
  }
}
