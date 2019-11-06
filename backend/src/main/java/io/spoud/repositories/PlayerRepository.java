package io.spoud.repositories;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import io.spoud.entities.PlayerEO;
import io.spoud.entities.QPlayerEO;

@Repository
public class PlayerRepository {

    public static final QPlayerEO PLAYER = QPlayerEO.playerEO;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    private static List<PlayerEO> db = Arrays.asList(PlayerEO.builder()
            .uuid(UUID.fromString("b480a9a6-00c5-11ea-8d71-362b9e155667"))
            .nickName("anna")
            .email("anna.messerli@spoud.io")
            .points(1111)
            .build(), PlayerEO.builder()
            .uuid(UUID.fromString("b480ac12-00c5-11ea-8d71-362b9e155667"))
            .nickName("gaetan")
            .email("gaetan.collaud@spoud.io")
            .points(2222)
            .build(), PlayerEO.builder()
            .uuid(UUID.fromString("b480ad5c-00c5-11ea-8d71-362b9e155667"))
            .nickName("marcel")
            .email("marcel.baur@spoud.io")
            .points(3333)
            .build(), PlayerEO.builder()
            .uuid(UUID.fromString("b480ae88-00c5-11ea-8d71-362b9e155667"))
            .email("julian.stampfli@spoud.io")
            .nickName("julian")
            .points(4444)
            .build(), PlayerEO.builder()
            .uuid(UUID.fromString("b480afb4-00c5-11ea-8d71-362b9e155667"))
            .email("lukas.zaugg@spoud.io")
            .nickName("Lukas")
            .points(5555)
            .build(), PlayerEO.builder()
            .uuid(UUID.fromString("b480b2b6-00c5-11ea-8d71-362b9e155667"))
            .email("patrick.boenzli@spoud.io")
            .nickName("patrick")
            .points(6666)
            .build());

    public List<PlayerEO> getAllPlayers() {
        return jpaQueryFactory.selectFrom(PLAYER).fetch();
    }

    public Optional<PlayerEO> findByUuid(UUID uuid) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(PLAYER).where(PLAYER.uuid.eq(uuid)).fetchOne());
    }
}
