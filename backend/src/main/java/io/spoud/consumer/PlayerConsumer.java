package io.spoud.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.spoud.data.kafka.Player;
import io.spoud.repositories.PlayerRepository;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
@Slf4j
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
      log.warn("Invalid input: `{}`. resulted in exception: `{}`", input, e);
      return "";
    }
  }
}
