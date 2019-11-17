package io.spoud.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spoud.entities.MatchEO;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.LinkedBlockingQueue;

@ApplicationScoped
@Slf4j
public class ResultProducer {

  @Inject
  private ObjectMapper mapper;

  private BlockingQueue<MatchEO> matchesQueue = new LinkedBlockingQueue<>();

  // TODO create a new entity to have players before and player after match
  public void add(MatchEO match) {
    log.info("Put match on the producer queue {}", match);
    matchesQueue.add(match);
  }

  @Outgoing("match-result")
  public CompletionStage<String> send() {
    log.info("Initializing kafka producer");
    return CompletableFuture.supplyAsync(() -> {
      try {
        MatchEO match = matchesQueue.take();
        log.info("Sending message to kafka with the message: {} ", match);
        return mapper.writeValueAsString(match);
      } catch (InterruptedException | JsonProcessingException e) {
        log.error("Unable to publish to kafka", e);
        return null;
      }
    });
  }
}
