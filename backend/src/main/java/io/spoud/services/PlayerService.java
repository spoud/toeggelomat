package io.spoud.services;

import io.spoud.data.entities.PlayerEO;
import io.spoud.data.kafka.PlayerBO;
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

  public void pushPlayer(PlayerEO player) {
    playerProducer.add(
        PlayerBO.builder()
            .uuid(player.getUuid())
            .email(player.getEmail())
            .nickName(player.getNickName())
            .slackId(player.getNickName())
            .build());
  }

  public List<PlayerEO> getAll(){
    return playerRepository.getAllPlayers();
  }

  @PostConstruct
  public void pushAll() {
    log.warn("push all");
    playerRepository.getAllPlayers().forEach(this::pushPlayer);
  }
}
