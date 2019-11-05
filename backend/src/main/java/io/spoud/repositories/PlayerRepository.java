package io.spoud.repositories;

import io.spoud.entities.PlayerEO;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class PlayerRepository {

    private static List<PlayerEO> db = Arrays.asList(
        PlayerEO.builder()
            .uuid(UUID.randomUUID())
            .nickName("anna")
            .email("anna.knifely@spoud.io")
            .points(1111)
            .build(),
        PlayerEO.builder()
            .uuid(UUID.randomUUID())
            .nickName("gaetan")
            .email("gaetan.however_that_is_spelled@spoud.io")
            .points(2222)
            .build(),
        PlayerEO.builder()
            .uuid(UUID.randomUUID())
            .nickName("marcel")
            .email("marcelo.lastname@spoud.io")
            .points(3333)
            .build(),
            PlayerEO.builder()
                .uuid(UUID.randomUUID())
                .email("julian.thegreatest@spoud.io")
                    .nickName("julian")
                .points(9999)
                    .build()
    );

    public List<PlayerEO> getAllPlayers() {
        return db;
    }

    public Optional<PlayerEO> findByUuid(UUID uuid){
        return db.stream()
                .filter(p -> p.getUuid().equals(uuid))
                .findFirst();
    }
}
