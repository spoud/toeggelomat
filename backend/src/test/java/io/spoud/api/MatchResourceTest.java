package io.spoud.api;

import io.quarkus.test.junit.QuarkusTest;
import io.spoud.api.data.MatchTO;
import io.spoud.api.data.SaveScoreInput;
import io.spoud.api.data.SeasonTO;
import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.entities.SeasonEO;
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

  @Inject
  SeasonResource seasonResource;

  private static final List<UUID> SEED_PLAYER_UUIDS = List.of(
    UUID.fromString("f7ec8a9c-09d6-11ea-8d71-362b9e155667"),
    UUID.fromString("b480afb4-00c5-11ea-8d71-362b9e155667"),
    UUID.fromString("b480ac12-00c5-11ea-8d71-362b9e155667"),
    UUID.fromString("b480a9a6-00c5-11ea-8d71-362b9e155667"));

  @AfterEach
  @Transactional
  void cleanup() {
    MatchEO.deleteAll();
    SeasonEO.deleteAll();
    PlayerEO.update(
        "offensePoints = 500, defensePoints = 500 where uuid in ?1", SEED_PLAYER_UUIDS);
  }

  private SaveScoreInput sampleScore() {
    return new SaveScoreInput(
      1,
      7,
      UUID.fromString("f7ec8a9c-09d6-11ea-8d71-362b9e155667"),
      UUID.fromString("b480afb4-00c5-11ea-8d71-362b9e155667"),
      UUID.fromString("b480ac12-00c5-11ea-8d71-362b9e155667"),
      UUID.fromString("b480a9a6-00c5-11ea-8d71-362b9e155667"));
  }

  @Test
  void should_return_empty_matches() {
    List<MatchTO> matches = matchResource.lastMaches(null);
    assertThat(matches).isEmpty();
  }

  @Test
  void should_save_a_match() {
    SaveScoreInput score = sampleScore();

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

  @Test
  void should_leave_match_unassigned_when_no_active_season() {
    matchResource.finishMatch(sampleScore());

    assertThat(matchResource.lastMaches(null)).hasSize(1);
  }

  @Test
  void should_tag_new_matches_with_the_active_season() {
    SeasonTO season = seasonResource.createSeason("Spring 2026");

    matchResource.finishMatch(sampleScore());

    assertThat(matchResource.lastMaches(season.uuid())).hasSize(1);
  }

  @Test
  void should_filter_matches_by_season() {
    SeasonTO seasonOne = seasonResource.createSeason("Season One");
    matchResource.finishMatch(sampleScore());

    SeasonTO seasonTwo = seasonResource.createSeason("Season Two");
    matchResource.finishMatch(sampleScore());

    assertThat(matchResource.lastMaches(seasonOne.uuid())).hasSize(1);
    assertThat(matchResource.lastMaches(seasonTwo.uuid())).hasSize(1);
    assertThat(matchResource.lastMaches(null)).hasSize(2);
  }
}
