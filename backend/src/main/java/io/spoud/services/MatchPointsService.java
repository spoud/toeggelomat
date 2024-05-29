package io.spoud.services;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class MatchPointsService {

  public static final int ADDITIONAL_POINT_FOR_LOSING = 5;
  public static final int ADDITIONAL_POINT_FOR_WINNING = 0;
  private static final double LI_POINTS_SLOPE = 4;
  private static final double LI_POINTS_OFFSET = -1.5;
  private static final int BASE_POINTS = 40;

  private final PlayerRepository playerRepository;

  private static double clamp(double d) {
    return Math.max(Math.min(d, 1.0), 0);
  }

  public static double slope(double looserPoints, double totalPoints) {
    return clamp(
        LI_POINTS_OFFSET + LI_POINTS_SLOPE * (double) (looserPoints) / (double) (totalPoints));
  }

  public MatchEO computePointsAndUpdatePlayers(MatchEO matchEO) {
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchEO);
    int points = calcPoints(playersHelper);

    playersHelper
        .getWinnerDeffence()
        .setDefensePoints(
            playersHelper.getWinnerDeffence().getDefensePoints()
                + points
                + ADDITIONAL_POINT_FOR_WINNING);
    playersHelper
        .getWinnerOffense()
        .setOffensePoints(
            playersHelper.getWinnerOffense().getOffensePoints()
                + points
                + ADDITIONAL_POINT_FOR_WINNING);
    playersHelper
        .getLooserDeffence()
        .setDefensePoints(
            playersHelper.getLooserDeffence().getDefensePoints()
                - points
                + ADDITIONAL_POINT_FOR_LOSING);
    playersHelper
        .getLooserOffense()
        .setOffensePoints(
            playersHelper.getLooserOffense().getOffensePoints()
                - points
                + ADDITIONAL_POINT_FOR_LOSING);

    ZonedDateTime now = ZonedDateTime.now();
    playersHelper
        .getAll()
        .forEach(
            p -> {
              p.setLastMatchTime(now);
              playerRepository.updatePointsAndLastMatch(p);
            });

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

  private int calcPoints(PlayersHelper playersHelper) {
    double factor = 1.0;
    factor *= sevenToZeroMultiplier(playersHelper.getMatch());
    double winnerPoints =
        playersHelper.getWinnerDeffence().getDefensePoints()
            + playersHelper.getWinnerOffense().getOffensePoints();
    double looserPoints =
        playersHelper.getLooserDeffence().getDefensePoints()
            + playersHelper.getLooserOffense().getOffensePoints();
    double total = winnerPoints + looserPoints;
    double slope = slope(looserPoints, total);
    return (int) Math.round(slope * factor * BASE_POINTS);
  }

  private double sevenToZeroMultiplier(MatchEO match) {
    return match.getBlueScore() != null
            && match.getRedScore() != null
            && (match.getBlueScore() == 0 || match.getRedScore() == 0)
        ? 2
        : 1;
  }

//  private double overTimeMultiplier(MatchEO match) {
//    return match.getBlueScore() != null
//            && match.getRedScore() != null
//            && (match.getBlueScore() == 0 || match.getRedScore() == 0)
//        ? 2
//        : 1;
//  }

  @Getter
  public static class PlayersHelper {
    private final PlayerEO blueDeffense;
    private final PlayerEO blueOffense;
    private final PlayerEO redDeffense;
    private final PlayerEO redOffense;
    private final MatchEO match;

    @Setter private boolean wonByBlue;

    public PlayersHelper(PlayerRepository playerRepository, MatchEO match) {
      this.match = match;
      this.wonByBlue =
          match.getRedScore() != null && match.getBlueScore() != null
              ? match.getBlueScore() > match.getRedScore()
              : true;
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
