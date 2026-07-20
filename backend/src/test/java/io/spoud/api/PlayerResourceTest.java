package io.spoud.api;

import io.quarkus.test.junit.QuarkusTest;
import io.spoud.api.data.PlayerTO;
import io.spoud.entities.PlayerEO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class PlayerResourceTest {

  private static final List<UUID> SEED_PLAYER_UUIDS = List.of(
    UUID.fromString("f7ec8a9c-09d6-11ea-8d71-362b9e155667"),
    UUID.fromString("b480afb4-00c5-11ea-8d71-362b9e155667"),
    UUID.fromString("b480ac12-00c5-11ea-8d71-362b9e155667"),
    UUID.fromString("b480a9a6-00c5-11ea-8d71-362b9e155667"));

  @Inject
  PlayerResource playerResource;

  @AfterEach
  @Transactional
  void cleanup() {
    PlayerEO.delete("uuid not in ?1", SEED_PLAYER_UUIDS);
    PlayerEO.update("active = true where uuid in ?1", SEED_PLAYER_UUIDS);
  }

  @Test
  void should_return_player() {
    List<PlayerTO> players = playerResource.findAll();
    assertThat(players).hasSize(4);
  }

  @Test
  void should_create_a_player() {
    PlayerTO created = playerResource.createPlayer("Testy");

    assertThat(created.uuid()).isNotNull();
    assertThat(created.nickName()).isEqualTo("Testy");
    assertThat(created.defensePoints()).isEqualTo(500);
    assertThat(created.offensePoints()).isEqualTo(500);
    assertThat(playerResource.findAll()).hasSize(5);
  }

  @Test
  void should_archive_a_player() {
    PlayerTO created = playerResource.createPlayer("Testy");

    boolean deleted = playerResource.deletePlayer(created.uuid());

    assertThat(deleted).isTrue();
    assertThat(playerResource.findAll()).hasSize(4);
  }

  @Test
  void should_return_false_when_archiving_unknown_player() {
    boolean deleted = playerResource.deletePlayer(UUID.randomUUID());

    assertThat(deleted).isFalse();
  }
}
