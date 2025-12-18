package io.spoud.api;

import io.quarkus.test.junit.QuarkusTest;
import io.spoud.api.data.MatchTO;
import io.spoud.api.data.SaveScoreInput;
import io.spoud.entities.MatchEO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class MatchResourceTest {
  @Inject
  MatchResource matchResource;

  @AfterEach
  @Transactional
  void cleanup() {
    MatchEO.deleteAll();
  }

  @Test
  void should_return_empty_matches() {
    List matches = matchResource.lastMaches();
    assertThat(matches).isEmpty();
  }

  @Test
  void should_save_a_match() {
    SaveScoreInput score = new SaveScoreInput(
      1,
      7,
      UUID.fromString("f7ec8a9c-09d6-11ea-8d71-362b9e155667"),
      UUID.fromString("b480afb4-00c5-11ea-8d71-362b9e155667"),
      UUID.fromString("b480ac12-00c5-11ea-8d71-362b9e155667"),
      UUID.fromString("b480a9a6-00c5-11ea-8d71-362b9e155667"));

    MatchTO saved = matchResource.finishMatch(score);

    assertThat(saved.uuid()).isNotNull();
    assertThat(saved.blueScore()).isEqualTo(7);
    assertThat(saved.redScore()).isEqualTo(1);
    assertThat(saved.points()).isEqualTo(20);
    assertThat(saved.playerRedDefenseUuid()).isEqualTo(score.playerRedDefenseUuid());
    assertThat(saved.playerRedOffenseUuid()).isEqualTo(score.playerRedOffenseUuid());
    assertThat(saved.playerBlueDefenseUuid()).isEqualTo(score.playerBlueDefenseUuid());
    assertThat(saved.playerBlueOffenseUuid()).isEqualTo(score.playerBlueOffenseUuid());
  }

}
