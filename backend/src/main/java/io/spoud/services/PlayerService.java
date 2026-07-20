package io.spoud.services;

import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class PlayerService {

  private static final int STARTING_POINTS = 500;

  @Inject PlayerRepository playerRepository;

  public PlayerEO createPlayer(String nickName) {
    PlayerEO player = new PlayerEO();
    player.uuid = UUID.randomUUID();
    player.nickName = nickName;
    player.defensePoints = STARTING_POINTS;
    player.offensePoints = STARTING_POINTS;
    player.active = true;
    playerRepository.persistAndFlush(player);
    return player;
  }

  public boolean archivePlayer(UUID uuid) {
    PlayerEO player = playerRepository.findById(uuid);
    if (player == null) {
      return false;
    }
    player.active = false;
    playerRepository.persistAndFlush(player);
    return true;
  }

  public boolean unarchivePlayer(UUID uuid) {
    PlayerEO player = playerRepository.findById(uuid);
    if (player == null) {
      return false;
    }
    player.active = true;
    playerRepository.persistAndFlush(player);
    return true;
  }

  public void resetAllPointsToStarting() {
    playerRepository.resetAllPoints(STARTING_POINTS);
  }
}
