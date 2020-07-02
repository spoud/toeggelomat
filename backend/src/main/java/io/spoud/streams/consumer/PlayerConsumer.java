package io.spoud.streams.consumer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.spoud.data.PlayerBO;
import io.spoud.repositories.PlayerRepository;
import io.spoud.services.EventService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
@Slf4j
public class PlayerConsumer {
  @Inject private PlayerRepository playerRepository;

  @Inject EventService eventService;

  @Incoming("player-in")
  @Acknowledgment(Acknowledgment.Strategy.NONE)
  public CompletionStage<KafkaRecord> store(KafkaRecord<String, PlayerBO> record) {
    record.getPayload().setUuid(UUID.fromString(record.getKey()));
    log.info("Player update received {}", record.getPayload());
    playerRepository.save(record.getPayload());
//    eventService.scoreChangedEvent();
    return CompletableFuture.completedFuture(record);
  }
}
