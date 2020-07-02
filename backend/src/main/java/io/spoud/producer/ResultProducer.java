package io.spoud.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spoud.data.kafka.MatchResultBO;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.LinkedBlockingQueue;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
@Slf4j
public class ResultProducer {

  @Inject private ObjectMapper mapper;

  private final BlockingQueue<MatchResultBO> matchesQueue = new LinkedBlockingQueue<>();

  public void add(MatchResultBO match) {
    log.info("Put match on the producer queue {}", match);
    matchesQueue.add(match);
  }

  @Outgoing("match-result")
  public CompletionStage<MatchResultBO> send() {
    log.info("Initializing kafka producer");
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            MatchResultBO match = matchesQueue.take();
            log.info("Sending message to kafka with the message: {} ", match);
            return match;
          } catch (InterruptedException e) {
            log.error("Unable to publish to kafka", e);
            return null;
          }
        });
  }
}
