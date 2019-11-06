package io.spoud.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spoud.entities.MatchEO;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.LinkedBlockingQueue;
import javax.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
@Slf4j
public class ResultProducer {

  ObjectMapper mapper = new ObjectMapper();

  private BlockingQueue<MatchEO> messages = new LinkedBlockingQueue<>();

  public void add(MatchEO message) {
    messages.add(message);
  }

  @Outgoing("match-result")
  public CompletionStage<String> send() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        MatchEO message = messages.take();
        log.info("Sending message to kafka with the message: " + message.toString());
        return mapper.writeValueAsString(message);
      } catch (InterruptedException | JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
