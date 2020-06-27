package io.spoud.repositories;

import io.spoud.data.kafka.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlayerRepository {

  public static Map<UUID, Player> repo = new ConcurrentHashMap<>();

  public Player save(Player player) {
    if (player.getUuid() == null) {
      player.setUuid(UUID.randomUUID());
    }
    return repo.put(player.getUuid(), player);
  }

  public List<Player> getAllPlayers() {
    return new ArrayList<>(repo.values());
  }

  public Player findByUuid(UUID uuid) {
    return repo.get(uuid);
  }

  public List<Player> findByUuids(List<UUID> uuid) {
    return uuid.stream().map(repo::get).collect(Collectors.toList());
  }
}
