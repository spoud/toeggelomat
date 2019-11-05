package io.spoud.producer;

import io.smallrye.reactive.messaging.kafka.KafkaMessage;
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

  private BlockingQueue<MatchEO> messages = new LinkedBlockingQueue<>();

  public void add(MatchEO message) {
    messages.add(message);
  }


  @Outgoing("results")
  public CompletionStage<KafkaMessage<String, MatchEO>> send() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        MatchEO message = messages.take();
        log.info("Sending message to kafka with the message: " + message.toString());
        return KafkaMessage.of("results", "key", message);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
