package io.spoud.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spoud.data.kafka.PlayerBO;
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
public class PlayerProducer {

  @Inject private ObjectMapper mapper;

  private final BlockingQueue<PlayerBO> playerQueue = new LinkedBlockingQueue<>();

  public void add(PlayerBO player) {
    log.info("Put match on the producer queue {}", player);
    playerQueue.add(player);
  }

  @Outgoing("player")
  public CompletionStage<String> send() {
    log.info("Initializing kafka producer");
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            PlayerBO player = playerQueue.take();
            log.info("Sending message to kafka with the message: {} ", player);
            return mapper.writeValueAsString(player);
          } catch (InterruptedException | JsonProcessingException e) {
            log.error("Unable to publish to kafka", e);
            return null;
          }
        });
  }
}
