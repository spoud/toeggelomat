package io.spoud.services;

import io.spoud.api.data.PlayerTO;
import io.spoud.repositories.PlayerRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Batches player lookups within a single GraphQL request: loads every player once on first
 * access instead of one query per match/team, so resolving offense/defense players for a list of
 * matches costs one query total rather than 4 per match.
 */
@RequestScoped
public class PlayerLookupService {

  @Inject PlayerRepository playerRepository;

  private Map<UUID, PlayerTO> playersByUuid;

  // SmallRye GraphQL resolves @Source fields concurrently, so lazy init must be synchronized.
  public synchronized PlayerTO getByUuid(UUID uuid) {
    if (playersByUuid == null) {
      Map<UUID, PlayerTO> loaded = new HashMap<>();
      playerRepository.listAll().forEach(player -> loaded.put(player.uuid, PlayerTO.from(player)));
      playersByUuid = loaded;
    }
    return playersByUuid.get(uuid);
  }
}
