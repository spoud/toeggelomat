package io.spoud.producer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.LinkedBlockingQueue;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Outgoing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.spoud.entities.MatchEO;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class ResultProducer {

  ObjectMapper mapper = new ObjectMapper();

  private BlockingQueue<MatchEO> messages = new LinkedBlockingQueue<>();

  // TODO create a new entity to have players before and player after match
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
