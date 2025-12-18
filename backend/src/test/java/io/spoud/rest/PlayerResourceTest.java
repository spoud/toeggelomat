package io.spoud.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.spoud.entities.PlayerEO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class PlayerResourceTest {

  @Test
  void should_return_player() {
    List<PlayerEO> players = given()
      .when().get("/api/v1/players")
      .then()
      .statusCode(200)
      .extract()
      .body().as(List.class);
    assertThat(players).hasSize(4);
  }
}
