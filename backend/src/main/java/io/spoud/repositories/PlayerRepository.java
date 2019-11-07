package io.spoud.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.spoud.entities.PlayerEO;
import io.spoud.entities.QPlayerEO;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerRepository {

    public static final QPlayerEO PLAYER = QPlayerEO.playerEO;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @ConfigProperty(name = "filename")
    String filename;

    private static List<PlayerEO> db;

    public PlayerRepository() {
        defaults();
    }

    private void defaults() {
        db = Arrays.asList(
            PlayerEO.builder()
                .uuid(UUID.randomUUID())
                .nickName("anna")
                .email("anna.knifely@spoud.io")
                .defensePoints(1111)
                .offensePoints(2222)

                .build(),
            PlayerEO.builder()
                .uuid(UUID.randomUUID())
                .nickName("gaetan")
                .email("gaetan.however_that_is_spelled@spoud.io")
                .defensePoints(2222)
                .offensePoints(2222)
                .build(),
            PlayerEO.builder()
                .uuid(UUID.randomUUID())
                .nickName("marcel")
                .email("marcelo.lastname@spoud.io")
                .defensePoints(3333)
                .offensePoints(2222)

                .build(),
            PlayerEO.builder()
                .uuid(UUID.randomUUID())
                .email("julian.thegreatest@spoud.io")
                .nickName("julian")
                .defensePoints(9999)
                .offensePoints(2222)

                .build(),
            PlayerEO.builder()
                .uuid(UUID.fromString("b480afb4-00c5-11ea-8d71-362b9e155667"))
                .email("lukas.zaugg@spoud.io")
                .nickName("Lukas")
                .defensePoints(5555)
                .offensePoints(2222)

                .build(),
            PlayerEO.builder()
                .uuid(UUID.fromString("b480b2b6-00c5-11ea-8d71-362b9e155667"))
                .email("patrick.boenzli@spoud.io")
                .nickName("patrick")
                .defensePoints(6666)
                .offensePoints(2222)

                .build()
        );

    }

    public void updatePointsOfPlayer(PlayerEO player) {
        jpaQueryFactory.update(PLAYER).where(PLAYER.uuid.eq(player.getUuid()))
            .set(PLAYER.offensePoints, player.getOffensePoints())
            .set(PLAYER.defensePoints, player.getDefensePoints())
            .execute();
    }
    public List<PlayerEO> getAllPlayers() {
        return jpaQueryFactory.selectFrom(PLAYER).fetch();
    }

    public Optional<PlayerEO> findByUuid(UUID uuid) {
        return Optional
            .ofNullable(jpaQueryFactory.selectFrom(PLAYER).where(PLAYER.uuid.eq(uuid)).fetchOne());
    }
}
