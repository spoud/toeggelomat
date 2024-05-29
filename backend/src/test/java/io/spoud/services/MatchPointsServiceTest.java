package io.spoud.services;

import io.quarkus.test.junit.QuarkusTest;
import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class MatchPointsServiceTest extends AbstractServiceTest {
  static PlayerEO createPlayer(String name) {
    return PlayerEO.builder().uuid(UUID.randomUUID())
      .email(name + "@spoud.io")
      .nickName(name)
      .offensePoints(500).defensePoints(500).build();
  }

  @Inject
  MatchPointsService matchPointsService;
  @Inject
  PlayerRepository playerRepository;
  List<PlayerEO> players;

  @BeforeEach
  @Transactional
  void setup() {
    players = List.of(
      playerRepository.insert(createPlayer("a")),
      playerRepository.insert(createPlayer("b")),
      playerRepository.insert(createPlayer("c")),
      playerRepository.insert(createPlayer("d"))
    );
  }

  @Test
  void should_compute_potential_point() {
    MatchEO match = createMatch(5, 7);
    MatchEO matchEO = matchPointsService.computePotentialPoints(match);
    assertThat(matchEO.getPotentialRedPoints()).isEqualTo(20);
    assertThat(matchEO.getPotentialBluePoints()).isEqualTo(20);
  }

  @Test
  void should_multiply_for_seven_zero() {
    MatchEO match = createMatch(0, 7);
    MatchEO matchEO = matchPointsService.computePotentialPoints(match);
    matchEO = matchPointsService.computePointsAndUpdatePlayers(matchEO);
    assertThat(matchEO.getPoints()).isEqualTo(40);
  }

  MatchEO createMatch(int blueScore, int redScore) {
    return MatchEO.builder()
      .blueScore(blueScore)
      .redScore(redScore)
      .playerBlueDefenseUuid(players.get(0).getUuid())
      .playerBlueOffenseUuid(players.get(1).getUuid())
      .playerRedDefenseUuid(players.get(2).getUuid())
      .playerRedOffenseUuid(players.get(3).getUuid())
      .build();
  }
}
