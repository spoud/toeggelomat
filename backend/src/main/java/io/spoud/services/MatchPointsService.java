package io.spoud.services;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatchPointsService {

  @Inject
  private PlayerRepository playerRepository;

  public MatchEO computePotentialPoints(MatchEO matchEO) {
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchEO);
    int points = computePotentialPoints(playersHelper);
    matchEO.setPoints(points);
    return matchEO;
  }

  private int computePotentialPoints(PlayersHelper playersHelper) {
    double winnerPoints =
      playersHelper.getWinnerDeffence().getDefensePoints() + playersHelper.getWinnerOffense()
        .getOffensePoints();
    double looserPoints =
      playersHelper.getLooserDeffence().getDefensePoints() + playersHelper.getLooserOffense()
        .getOffensePoints();
    double total = winnerPoints + looserPoints;
    int point = (int) ((winnerPoints / total) * 40);
    return point;
  }

  private void calc(MatchEO match, PlayerEO winnerOffense, PlayerEO winnerDefense,
    PlayerEO looserOffense, PlayerEO looserDefense, boolean wonByZero, boolean dryRun) {

    double winnerPoints = winnerDefense.getDefensePoints() + winnerOffense.getOffensePoints();
    double looserPoints = looserDefense.getDefensePoints() + looserOffense.getOffensePoints();
    double total = winnerPoints + looserPoints;
    int winnings = (int) ((winnerPoints / total) * 40) * (wonByZero ? 2 : 1);

  }

  @Getter
  public class PlayersHelper {
    private final PlayerEO blueDeffense;
    private final PlayerEO blueOffense;
    private final PlayerEO redDeffense;
    private final PlayerEO redOffense;
    private final MatchEO match;
    private final boolean wonByBlue;

    public PlayersHelper(PlayerRepository playerRepository, MatchEO match) {
      this.match = match;
      this.wonByBlue = match.getRedScore() != null && match.getBlueScore() != null ?
        match.getBlueScore() > match.getRedScore() : true;
      blueDeffense = playerRepository.findByUuid(match.getPlayerBlueDefenseUuid()).get();
      blueOffense = playerRepository.findByUuid(match.getPlayerBlueOffenseUuid()).get();
      redDeffense = playerRepository.findByUuid(match.getPlayerRedDefenseUuid()).get();
      redOffense = playerRepository.findByUuid(match.getPlayerRedOffenseUuid()).get();
    }

    public PlayerEO getWinnerDeffence() {
      return wonByBlue ? blueDeffense : redDeffense;
    }

    public PlayerEO getWinnerOffense() {
      return wonByBlue ? blueOffense : redOffense;
    }

    public PlayerEO getLooserDeffence() {
      return !wonByBlue ? blueDeffense : redDeffense;
    }

    public PlayerEO getLooserOffense() {
      return !wonByBlue ? blueOffense : redOffense;
    }
  }
}
