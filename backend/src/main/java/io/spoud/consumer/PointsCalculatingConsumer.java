package io.spoud.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationScoped
public class PointsCalculatingConsumer {

  ObjectMapper mapper = new ObjectMapper();

  @Autowired
  PlayerRepository playerRepository;

  @Incoming("results")
  public CompletionStage<Void> consumeAsync(String input) {
    return CompletableFuture.runAsync(() -> {
      try {
        System.out.println(input);
        System.out.println("RESULTS STARTING");
        MatchEO match = mapper.readValue(input, MatchEO.class);
        System.out.println("MATCH PARSED");
        if (match.getRedScore() > match.getBlueScore()) {
          System.out.println("RED WON");
          calc(match,
              mapUuid(match.getPlayerRedOffenseUuid()),
              mapUuid(match.getPlayerRedDefenseUuid()),
              mapUuid(match.getPlayerBlueOffenseUuid()),
              mapUuid(match.getPlayerBlueDefenseUuid()),
              match.getBlueScore() == 0,
              false);
        } else {
          System.out.println("BLUE WON");
          calc(match,
              mapUuid(match.getPlayerBlueOffenseUuid()),
              mapUuid(match.getPlayerBlueDefenseUuid()),
              mapUuid(match.getPlayerRedOffenseUuid()),
              mapUuid(match.getPlayerRedDefenseUuid()),
              match.getRedScore() == 0,
              false);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  private PlayerEO mapUuid(UUID playerUuid) {
    System.out.println("START FETCH");
    return playerRepository.findByUuid(playerUuid).orElseThrow(() ->
        {
          System.out.println("you are stupid");
          return new IllegalArgumentException();
        }
    );
  }

  private void calc(MatchEO match,
      PlayerEO winnerOffense,
      PlayerEO winnerDefense,
      PlayerEO looserOffense,
      PlayerEO looserDefense,
      boolean wonByZero,
      boolean dryRun) {
    System.out.println("START CALC");

    double winnerPoints = winnerDefense.getDefensePoints() + winnerOffense.getOffensePoints();
    double looserPoints = looserDefense.getDefensePoints() + looserOffense.getOffensePoints();
    double total = winnerPoints + looserPoints;
    int winnings = (int) ((winnerPoints / total) * 40) * (wonByZero ? 2 : 1);
    System.out.println(winnings);
    if (!dryRun) {
      winnerOffense.setOffensePoints(winnerOffense.getOffensePoints() + winnings);
      winnerDefense.setDefensePoints(winnerDefense.getDefensePoints() + winnings);
      looserOffense.setOffensePoints(looserOffense.getOffensePoints() - winnings);
      looserDefense.setDefensePoints(looserDefense.getDefensePoints() - winnings);
      playerRepository.updatePointsOfPlayer(winnerOffense);
      playerRepository.updatePointsOfPlayer(winnerDefense);
      playerRepository.updatePointsOfPlayer(looserOffense);
      playerRepository.updatePointsOfPlayer(looserDefense);
    }
  }

}
