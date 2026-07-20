package io.spoud.api;

import io.quarkus.test.junit.QuarkusTest;
import io.spoud.api.data.SeasonTO;
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
class SeasonResourceTest {

  private static final List<UUID> SEED_PLAYER_UUIDS = List.of(
    UUID.fromString("f7ec8a9c-09d6-11ea-8d71-362b9e155667"),
    UUID.fromString("b480afb4-00c5-11ea-8d71-362b9e155667"),
    UUID.fromString("b480ac12-00c5-11ea-8d71-362b9e155667"),
    UUID.fromString("b480a9a6-00c5-11ea-8d71-362b9e155667"));

  @Inject
  SeasonResource seasonResource;

  @AfterEach
  @Transactional
  void cleanup() {
    SeasonEO.deleteAll();
    PlayerEO.update(
        "offensePoints = 500, defensePoints = 500 where uuid in ?1", SEED_PLAYER_UUIDS);
  }

  @Test
  void should_create_a_season() {
    SeasonTO season = seasonResource.createSeason("Spring 2026");

    assertThat(season.uuid()).isNotNull();
    assertThat(season.label()).isEqualTo("Spring 2026");
    assertThat(season.startTime()).isNotNull();
    assertThat(season.endTime()).isNull();
  }

  @Test
  void should_auto_close_the_previous_active_season_when_starting_a_new_one() {
    SeasonTO first = seasonResource.createSeason("Season One");

    SeasonTO second = seasonResource.createSeason("Season Two");

    List<SeasonTO> seasons = seasonResource.allSeasons();
    SeasonTO firstAfter =
        seasons.stream().filter(s -> s.uuid().equals(first.uuid())).findFirst().orElseThrow();
    SeasonTO secondAfter =
        seasons.stream().filter(s -> s.uuid().equals(second.uuid())).findFirst().orElseThrow();
    assertThat(firstAfter.endTime()).isNotNull();
    assertThat(secondAfter.endTime()).isNull();
  }

  @Test
  void should_close_a_season() {
    SeasonTO season = seasonResource.createSeason("Season One");

    boolean closed = seasonResource.closeSeason(season.uuid());

    assertThat(closed).isTrue();
    assertThat(seasonResource.allSeasons().get(0).endTime()).isNotNull();
  }

  @Test
  void should_return_false_when_closing_unknown_season() {
    assertThat(seasonResource.closeSeason(UUID.randomUUID())).isFalse();
  }

  @Test
  void should_return_false_when_closing_an_already_closed_season() {
    SeasonTO season = seasonResource.createSeason("Season One");
    seasonResource.closeSeason(season.uuid());

    assertThat(seasonResource.closeSeason(season.uuid())).isFalse();
  }

  @Test
  void should_list_seasons_newest_first() {
    seasonResource.createSeason("Season One");
    seasonResource.createSeason("Season Two");

    List<SeasonTO> seasons = seasonResource.allSeasons();

    assertThat(seasons).hasSize(2);
    assertThat(seasons.get(0).label()).isEqualTo("Season Two");
  }

  @Test
  @Transactional
  void should_reset_player_points_when_starting_a_new_season() {
    UUID playerUuid = SEED_PLAYER_UUIDS.get(0);
    PlayerEO.update("offensePoints = 700, defensePoints = 300 where uuid = ?1", playerUuid);

    seasonResource.createSeason("Season One");

    PlayerEO after = PlayerEO.findById(playerUuid);
    assertThat(after.offensePoints).isEqualTo(500);
    assertThat(after.defensePoints).isEqualTo(500);
  }
}
