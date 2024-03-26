package io.spoud.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spoud.services.EventService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;

@Slf4j
@Path("/api/v1/sse")
@ApplicationScoped
public class ServerSentEventResource {

  @Inject EventService eventService;

  @Inject ObjectMapper objectMapper;

  @GET
  @Path("matches")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public Publisher<String> newMatch() {
    return convertToJson(eventService.newMatchStream());
  }

  @GET
  @Path("scores")
  @Produces(MediaType.SERVER_SENT_EVENTS)
  public Publisher<String> scoreChanged() {
    return convertToJson(eventService.scoreChangedStream());
  }

  private Publisher<String> convertToJson(Publisher<?> objStream) {
    return ReactiveStreams.fromPublisher(objStream)
        .map(
            match -> {
              try {
                return objectMapper.writeValueAsString(match);
              } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
              }
            })
        .buildRs();
  }
}
