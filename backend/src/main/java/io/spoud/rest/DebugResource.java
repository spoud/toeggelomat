package io.spoud.rest;

import io.spoud.data.MatchPropositionBO;
import io.spoud.services.EventService;
import io.spoud.services.MatchService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
