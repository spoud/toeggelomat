package io.spoud.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spoud.entities.MatchEO;
import io.spoud.services.EventService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Slf4j
@Path("/api/v1/sse")
@ApplicationScoped
public class ServerSentEventResource {

    @Inject
    private EventService eventService;

    @Inject
    private ObjectMapper objectMapper;

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

    private Publisher<String> convertToJson(Publisher<?> objStream){
        return ReactiveStreams.fromPublisher(objStream)
                .map(match -> {
                    try {
                        return objectMapper.writeValueAsString(match);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .buildRs();
    }

}
