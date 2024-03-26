package io.spoud.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.spoud.entities.PlayerEO;
import io.spoud.entities.QPlayerEO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PlayerRepository {

  public static final QPlayerEO PLAYER = QPlayerEO.playerEO;

  @Inject JPAQueryFactory jpaQueryFactory;

  public void updatePointsAndLastMatch(PlayerEO player) {
    jpaQueryFactory
        .update(PLAYER)
        .where(PLAYER.uuid.eq(player.getUuid()))
        .set(PLAYER.offensePoints, player.getOffensePoints())
        .set(PLAYER.defensePoints, player.getDefensePoints())
        .set(PLAYER.lastMatchTime, player.getLastMatchTime())
        .execute();
  }

  public List<PlayerEO> getAllPlayers() {
    return jpaQueryFactory.selectFrom(PLAYER).fetch();
  }

  public Optional<PlayerEO> findByUuid(UUID uuid) {
    return Optional.ofNullable(
        jpaQueryFactory.selectFrom(PLAYER).where(PLAYER.uuid.eq(uuid)).fetchOne());
  }

  public List<PlayerEO> findByUuids(List<UUID> uuid) {
    return jpaQueryFactory
        .selectFrom(PLAYER)
        .where(PLAYER.uuid.in(uuid.toArray(new UUID[0])))
        .fetch();
  }
}
