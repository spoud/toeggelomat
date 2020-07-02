package io.spoud.streams.processor;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.spoud.data.PlayerBO;
import io.spoud.data.PointChangesBO;
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
  public KafkaRecord<String, PlayerBO> process(PointChangesBO pointChange) {
    PlayerBO player = playerRepository.findByUuid(pointChange.getPlayerUuid());
    player.setDefensePoints(player.getDefensePoints() + pointChange.getPointsDefense());
    player.setOffensePoints(player.getOffensePoints() + pointChange.getPointsOffense());
    player.setLastMatchTime(pointChange.getMatchTime());
    PlayerBO saved = playerRepository.save(player);
    return KafkaRecord.of(saved.getUuid().toString(), saved);
  }
}
