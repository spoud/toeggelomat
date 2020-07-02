package io.spoud.streams.consumer;

import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.spoud.data.kafka.MatchResultBO;
import io.spoud.data.kafka.PlayerBO;
import io.spoud.repositories.MatchRepository;
import io.spoud.repositories.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
@Slf4j
public class MatchConsumer {
  @Inject MatchRepository matchRepository;

  @Incoming("match-in")
  public CompletionStage<KafkaRecord> store(KafkaRecord<String, MatchResultBO> record) {
    MatchResultBO matchResult = record.getPayload();
    matchResult.setUuid(
        Optional.ofNullable(record.getKey()).map(UUID::fromString).orElseGet(UUID::randomUUID));
    log.info("Match result received {}:{}", matchResult);
    matchRepository.addMatch(matchResult);
    return CompletableFuture.completedFuture(record);
  }
}
