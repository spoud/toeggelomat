package io.spoud.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spoud.entities.PlayerEO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import io.spoud.entities.PlayerEO;
import io.spoud.entities.QPlayerEO;

@Repository
public class PlayerRepository {

    public static final QPlayerEO PLAYER = QPlayerEO.playerEO;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @ConfigProperty(name = "filename")
    String filename;

    private static List<PlayerEO> db;

    public PlayerRepository() {
        if (Files.exists(Path.of(filename))) {
            try {
                readFile();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        defaults();
    }

    private void defaults() {
        db = Arrays.asList(
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
                .build() PlayerEO.builder()
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

    }

    public void updatePlayer(PlayerEO player){
        db.removeIf(stored->stored.getUuid().equals(player.getUuid()));
        db.add(player);
    }

    public List<PlayerEO> getAllPlayers() {
        return jpaQueryFactory.selectFrom(PLAYER).fetch();
    }

    public Optional<PlayerEO> findByUuid(UUID uuid) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(PLAYER).where(PLAYER.uuid.eq(uuid)).fetchOne());
    }

    public List<PlayerEO> getAllPlayers() {
        return db;
    }

    public Optional<PlayerEO> findByUuid(UUID uuid){
        return db.stream()
            .filter(p -> p.getUuid().equals(uuid))
            .findFirst();
    }

    public void readFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File(filename);

        db = objectMapper.readValue(file, new TypeReference<List<PlayerEO>>() {
        });
    }


    public void writeFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        writeStringToFile(objectMapper.writeValueAsString(db));
    }

    private void writeStringToFile(String str)
        throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(str);

        writer.close();
    }
}
