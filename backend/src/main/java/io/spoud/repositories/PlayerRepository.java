package io.spoud.repositories;

import io.spoud.entities.PlayerEO;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PlayerRepository {

    private static List<PlayerEO> db = Arrays.asList(
            PlayerEO.builder()
                    .uuid(UUID.fromString("34ef53f8-ff1d-11e9-8f0b-362b9e155667"))
                    .nickName("gaetan")
                    .points(1000)
                    .build(),
            PlayerEO.builder()
                    .uuid(UUID.fromString("34ef5664-ff1d-11e9-8f0b-362b9e155667"))
                    .nickName("julian")
                    .points(1000)
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
