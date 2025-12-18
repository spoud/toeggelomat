package io.spoud.api;

import io.quarkus.test.junit.QuarkusTest;
import io.spoud.api.data.PlayerTO;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class PlayerResourceTest {

  @Inject
  PlayerResource playerResource;

  @Test
  void should_return_player() {
    List<PlayerTO> players = playerResource.findAll();
    assertThat(players).hasSize(4);
  }
}
