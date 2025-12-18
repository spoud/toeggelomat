package io.spoud.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.spoud.entities.MatchEO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class MatchResourceTest {

  @AfterEach
  @Transactional
  void cleanup() {
    MatchEO.deleteAll();
  }

  @Test
  void should_return_empty_matches() {
    List matches = given()
      .when().get("/api/v1/matches")
      .then()
      .statusCode(200)
      .extract()
      .body().as(List.class);
    assertThat(matches).isEmpty();
  }

  @Test
  void should_save_a_match() {
    MatchEO match = new MatchEO();
    match.uuid = UUID.randomUUID();
    match.blueScore = 7;
    match.redScore = 1;
    match.playerRedDefenseUuid = UUID.fromString("f7ec8a9c-09d6-11ea-8d71-362b9e155667");
    match.playerRedOffenseUuid = UUID.fromString("b480afb4-00c5-11ea-8d71-362b9e155667");
    match.playerBlueDefenseUuid = UUID.fromString("b480ac12-00c5-11ea-8d71-362b9e155667");
    match.playerBlueOffenseUuid = UUID.fromString("b480a9a6-00c5-11ea-8d71-362b9e155667");

    MatchEO saved = given()
      .contentType("application/json")
      .when()
      .body(match)
      .post("/api/v1/matches/set-score")
      .then()
      .statusCode(200)
      .extract()
      .body()
      .as(MatchEO.class);

    assertThat(saved.uuid).isEqualTo(match.uuid);
    assertThat(saved.blueScore).isEqualTo(7);
    assertThat(saved.redScore).isEqualTo(1);
    assertThat(saved.points).isEqualTo(20);
    assertThat(saved.playerRedDefenseUuid).isEqualTo(match.playerRedDefenseUuid);
    assertThat(saved.playerRedOffenseUuid).isEqualTo(match.playerRedOffenseUuid);
    assertThat(saved.playerBlueDefenseUuid).isEqualTo(match.playerBlueDefenseUuid);
    assertThat(saved.playerBlueOffenseUuid).isEqualTo(match.playerBlueOffenseUuid);
  }


}
