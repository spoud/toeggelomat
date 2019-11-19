package io.spoud.services;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MatchPointsService {

  public static final int ADDITIONAL_POINT_FOR_PLAYING = 1;

  @Inject
  private PlayerRepository playerRepository;

  public MatchEO computePointsAndUpdatePlayers(MatchEO matchEO) {
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchEO);
    int points = calcPoints(playersHelper);

    playersHelper.getWinnerDeffence()
      .setDefensePoints(playersHelper.getWinnerDeffence().getDefensePoints() + points
                        + ADDITIONAL_POINT_FOR_PLAYING);
    playersHelper.getWinnerOffense()
      .setOffensePoints(playersHelper.getWinnerOffense().getOffensePoints() + points
                        + ADDITIONAL_POINT_FOR_PLAYING);
    playersHelper.getLooserDeffence()
      .setDefensePoints(playersHelper.getLooserDeffence().getDefensePoints() - points
                        + ADDITIONAL_POINT_FOR_PLAYING);
    playersHelper.getLooserOffense()
      .setOffensePoints(playersHelper.getLooserOffense().getOffensePoints() - points
                        + ADDITIONAL_POINT_FOR_PLAYING);
    playersHelper.getAll().forEach(p -> playerRepository.updatePointsAndLastMatch(p));

    matchEO.setPoints(points);
    return matchEO;
  }

  public MatchEO computePotentialPoints(MatchEO matchEO) {
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchEO);
    playersHelper.setWonByBlue(true);
    matchEO.setPotentialBluePoints(calcPoints(playersHelper));
    playersHelper.setWonByBlue(false);
    matchEO.setPotentialRedPoints(calcPoints(playersHelper));
    return matchEO;
  }

  static private final double LI_POINTS_SLOPE = 4;
  static private final double LI_POINTS_OFFSET = -1.5;

  static private final int BASE_POINTS = 40;

  private int calcPoints(PlayersHelper playersHelper) {
    double factor = 1.0;
    factor *= zeroMultiplier(playersHelper.getMatch());
    factor *= birthdayMultiplier(playersHelper.getWinnerOffense());
    factor *= birthdayMultiplier(playersHelper.getWinnerDeffence());
    factor *= birthdayMultiplier(playersHelper.getLooserOffense());
    factor *= birthdayMultiplier(playersHelper.getLooserDeffence());
    double winnerPoints =
      playersHelper.getWinnerDeffence().getDefensePoints() + playersHelper.getWinnerOffense()
        .getOffensePoints();
    double looserPoints =
      playersHelper.getLooserDeffence().getDefensePoints() + playersHelper.getLooserOffense()
        .getOffensePoints();
    double total = winnerPoints + looserPoints;
    double slope = slope(looserPoints, total);
    return (int)Math.round(slope * factor * BASE_POINTS);
  }

  private static double clamp(double d) {
    return Math.max(Math.min(d, 1.0), 0);
  }

  public static double slope(double looserPoints, double totalPoints) {
    return clamp(
      LI_POINTS_OFFSET + LI_POINTS_SLOPE * (double) (looserPoints) / (double) (totalPoints));
  }

  private double birthdayMultiplier(PlayerEO player) {
    return 1; // add birthdays
  }

  private double zeroMultiplier(MatchEO match) {
    return match.getBlueScore() != null && match.getRedScore() != null && (match.getBlueScore() == 0
                                                                           || match.getRedScore()
                                                                              == 0) ? 2 : 1;
  }

  @Getter
  public class PlayersHelper {
    private final PlayerEO blueDeffense;
    private final PlayerEO blueOffense;
    private final PlayerEO redDeffense;
    private final PlayerEO redOffense;
    private final MatchEO match;

    @Setter
    private boolean wonByBlue;

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

    public List<PlayerEO> getAll() {
      return Arrays.asList(blueDeffense, blueOffense, redDeffense, redOffense);
    }

  }
}
