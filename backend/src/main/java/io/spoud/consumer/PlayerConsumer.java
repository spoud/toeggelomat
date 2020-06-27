package io.spoud.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.spoud.data.kafka.Player;
import io.spoud.repositories.PlayerRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.apache.kafka.common.InvalidRecordException;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class PlayerConsumer {
  @Inject private ObjectMapper mapper;

  @Inject private PlayerRepository playerRepository;

  @Incoming("player-in")
  @Blocking
  @Transactional
  public String store(String input) {
    try {
      return mapper.writeValueAsString(
          playerRepository.save(mapper.readValue(input, Player.class)));

    } catch (JsonProcessingException e) {
      throw new InvalidRecordException(input, e);
    }
  }
}
