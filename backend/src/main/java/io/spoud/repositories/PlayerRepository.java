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
            .points(1111)
            .build(),
        PlayerEO.builder()
            .uuid(UUID.randomUUID())
            .nickName("gaetan")
            .points(2222)
            .build(),
        PlayerEO.builder()
            .uuid(UUID.randomUUID())
            .nickName("marcel")
            .points(3333)
            .build(),
            PlayerEO.builder()
                .uuid(UUID.randomUUID())
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
