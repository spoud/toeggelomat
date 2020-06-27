package io.spoud.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.spoud.repositories.PlayerRepository;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
@Slf4j
public class PointChangeConsumer {
  @Inject private ObjectMapper mapper;

  @Inject private PlayerRepository playerRepository;

  @Incoming("point-change")
  @Outgoing("player")
  @Broadcast
  @Blocking
  @Transactional
  public String store(String input) {
    try {
      var pointChange = mapper.readTree(input);
      var player =
          playerRepository.findByUuid(UUID.fromString(pointChange.get("PLAYERUUID").asText()));
      player.setDefensePoints(
          player.getDefensePoints() + pointChange.get("POINTS_DEFENSE").asInt());
      player.setOffensePoints(
          player.getOffensePoints() + pointChange.get("POINTS_OFFENSE").asInt());
      player.setLastMatchTime(ZonedDateTime.parse(pointChange.get("MATCHTIME").asText()));
      return mapper.writeValueAsString(playerRepository.save(player));
    } catch (JsonProcessingException e) {
      log.warn("Invalid input: `{}`. resulted in exception: `{}`", input, e);
      return "";
    }
  }
}
