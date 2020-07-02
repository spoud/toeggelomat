package io.spoud.repositories;

import io.spoud.data.kafka.PlayerBO;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerRepository {

  public static Map<UUID, PlayerBO> repo = new ConcurrentHashMap<>();

  public PlayerBO save(PlayerBO player) {
    if (player.getUuid() == null) {
      player.setUuid(UUID.randomUUID());
    }
    return repo.put(player.getUuid(), player);
  }

  public List<PlayerBO> getAllPlayers() {
    return new ArrayList<>(repo.values());
  }

  public PlayerBO findByUuid(UUID uuid) {
    return repo.get(uuid);
  }

  public List<PlayerBO> findByUuids(List<UUID> uuid) {
    return uuid.stream().map(repo::get).collect(Collectors.toList());
  }
}
