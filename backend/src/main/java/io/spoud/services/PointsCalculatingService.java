package io.spoud.services;


import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

public class PointsCalculatingService {

  private final double percOfWinnings = 0.01;
  PlayerEO blueDefense = new PlayerEO();
  PlayerEO blueOffense = new PlayerEO();
  PlayerEO redDefense = new PlayerEO();
  PlayerEO redOffense = new PlayerEO();
  @Autowired
  private PlayerRepository playerRepository;
  private int bluePoints;
  private int redPoints;
  private int gamePoints;

  public List<PlayerEO> calculatePoints(final MatchEO matchResult) {
    blueDefense = playerRepository.findByUuid(matchResult.getPlayerBlueDefenseUuid())
        .orElseThrow(() -> new NotFoundException(
            "User not found: " + matchResult.getPlayerBlueDefenseUuid()));
    blueOffense = playerRepository.findByUuid(matchResult.getPlayerBlueOffenseUuid())
        .orElseThrow(() -> new NotFoundException(
            "User not found: " + matchResult.getPlayerBlueOffenseUuid()));
    redDefense = playerRepository.findByUuid(matchResult.getPlayerRedDefenseUuid())
        .orElseThrow(() -> new NotFoundException(
            "User not found: " + matchResult.getPlayerRedDefenseUuid()));
    redOffense = playerRepository.findByUuid(matchResult.getPlayerRedOffenseUuid())
        .orElseThrow(() -> new NotFoundException(
            "User not found: " + matchResult.getPlayerRedOffenseUuid()));

    bluePoints = blueDefense.getPoints() + blueOffense.getPoints();
    redPoints = redDefense.getPoints() + redOffense.getPoints();

    if (matchResult.getRedScore() > matchResult.getBlueScore()) {
      gamePoints = (int)(bluePoints * percOfWinnings);
      redDefense.setPoints(redDefense.getPoints() + gamePoints);
      redOffense.setPoints(redOffense.getPoints() + gamePoints);
      blueDefense.setPoints(blueDefense.getPoints() - gamePoints);
      blueOffense.setPoints(blueOffense.getPoints() - gamePoints);
    } else if (matchResult.getBlueScore() > matchResult.getRedScore()) {
      gamePoints = (int)(redPoints * percOfWinnings);
      redDefense.setPoints(redDefense.getPoints() - gamePoints);
      redOffense.setPoints(redOffense.getPoints() - gamePoints);
      blueDefense.setPoints(blueDefense.getPoints() + gamePoints);
      blueOffense.setPoints(blueOffense.getPoints() + gamePoints);
    }
    final List<PlayerEO> matchPlayers = Arrays
        .asList(blueDefense, blueOffense, redDefense, redOffense);
    return matchPlayers;
  }
}
