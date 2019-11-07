package io.spoud.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.spoud.entities.PlayerEO;
import io.spoud.entities.QPlayerEO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerRepository {

    public static final QPlayerEO PLAYER = QPlayerEO.playerEO;

    private List<PlayerEO> cache = null;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static List<PlayerEO> db;

    public void updatePointsOfPlayer(PlayerEO player) {
        jpaQueryFactory.update(PLAYER).where(PLAYER.uuid.eq(player.getUuid()))
            .set(PLAYER.offensePoints, player.getOffensePoints())
            .set(PLAYER.defensePoints, player.getDefensePoints())
            .execute();
    }
    public List<PlayerEO> getAllPlayers() {

        cache = jpaQueryFactory.selectFrom(PLAYER).fetch();
        return cache;
    }

    public Optional<PlayerEO> findByUuid(UUID uuid) {
        return (cache == null ? getAllPlayers() : cache).stream()
            .filter(x -> x.getUuid().equals(uuid)).findFirst();
    }
}
