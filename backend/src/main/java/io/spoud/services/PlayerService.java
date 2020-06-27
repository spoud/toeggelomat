package io.spoud.services;

import io.spoud.data.kafka.Player;
import io.spoud.producer.PlayerProducer;
import io.spoud.repositories.PlayerRepository;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Transactional
@Slf4j
public class PlayerService {
  @Inject private PlayerRepository playerRepository;

  @Inject private PlayerProducer playerProducer;

  public void pushPlayer(Player player) {
    playerProducer.add(player);
  }

  public List<Player> getAll() {
    return playerRepository.getAllPlayers();
  }

  @PostConstruct
  public void pushAll() {
    log.warn("push all");
    playerRepository.getAllPlayers().forEach(this::pushPlayer);
  }
}
