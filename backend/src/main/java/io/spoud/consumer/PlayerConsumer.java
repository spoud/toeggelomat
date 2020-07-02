package io.spoud.consumer;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.spoud.data.kafka.PlayerBO;
import io.spoud.repositories.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.Message;
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

  @Incoming("player-in")
  public CompletionStage<KafkaRecord> store(KafkaRecord<String, PlayerBO> record) {
    record.getPayload().setUuid(UUID.fromString(record.getKey()));
    log.info("Player update received {}:{}", record.getKey(), record.getPayload());
    playerRepository.save(record.getPayload());
    return CompletableFuture.completedFuture(record);
  }
}
