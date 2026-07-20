package io.spoud.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.spoud.entities.PlayerEO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PlayerRepository implements PanacheRepositoryBase<PlayerEO, UUID> {

  public List<PlayerEO> findAllActive() {
    return list("active", true);
  }

  public void updatePointsAndLastMatch(PlayerEO player) {
    update(
        "offensePoints = :offensePoints, defensePoints = :defensePoints, lastMatchTime = :lastMatchTime where uuid = :uuid",
        Parameters.with("offensePoints", player.offensePoints)
            .and("defensePoints", player.defensePoints)
            .and("lastMatchTime", player.lastMatchTime)
            .and("uuid", player.uuid));
  }
}
