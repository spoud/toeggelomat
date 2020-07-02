package io.spoud.processor;

import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.spoud.data.kafka.PlayerBO;
import io.spoud.data.kafka.PointChangesBO;
import io.spoud.repositories.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class PointChangeProcessor {
  @Inject private PlayerRepository playerRepository;

  @Incoming("point-change")
  @Outgoing("player")
  @Broadcast
  public PlayerBO store(PointChangesBO pointChange) {
    PlayerBO player = playerRepository.findByUuid(pointChange.getPlayerUuid());
    player.setDefensePoints(player.getDefensePoints() + pointChange.getPointsDefense());
    player.setOffensePoints(player.getOffensePoints() + pointChange.getPointsOffense());
    player.setLastMatchTime(pointChange.getMatchTime());
    return playerRepository.save(player);
  }
}
