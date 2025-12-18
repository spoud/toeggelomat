package io.spoud.services;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class MatchPointsService {

  public static final int ADDITIONAL_POINT_FOR_LOSING = 5;
  public static final int ADDITIONAL_POINT_FOR_WINNING = 0;
  private static final double LI_POINTS_SLOPE = 4;
  private static final double LI_POINTS_OFFSET = -1.5;
  private static final int BASE_POINTS = 40;

  @Inject public PlayerRepository playerRepository;

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

    playersHelper.getWinnerDeffence().defensePoints =
        playersHelper.getWinnerDeffence().defensePoints + points + ADDITIONAL_POINT_FOR_WINNING;
    playersHelper.getWinnerOffense().offensePoints =
        playersHelper.getWinnerOffense().offensePoints + points + ADDITIONAL_POINT_FOR_WINNING;
    playersHelper.getLooserDeffence().defensePoints =
        playersHelper.getLooserDeffence().defensePoints - points + ADDITIONAL_POINT_FOR_LOSING;
    playersHelper.getLooserOffense().offensePoints =
        playersHelper.getLooserOffense().offensePoints - points + ADDITIONAL_POINT_FOR_LOSING;

    ZonedDateTime now = ZonedDateTime.now();
    playersHelper
        .getAll()
        .forEach(
            p -> {
              p.lastMatchTime = now;
              playerRepository.updatePointsAndLastMatch(p);
            });

    matchEO.points = points;
    return matchEO;
  }

  public MatchEO computePotentialPoints(MatchEO matchEO) {
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchEO);
    playersHelper.wonByBlue = true;
    matchEO.potentialBluePoints = calcPoints(playersHelper);
    playersHelper.wonByBlue = false;
    matchEO.potentialRedPoints = calcPoints(playersHelper);
    return matchEO;
  }

  private int calcPoints(PlayersHelper playersHelper) {
    double factor = 1.0;
    factor *= sevenToZeroMultiplier(playersHelper.match);
    double winnerPoints =
        playersHelper.getWinnerDeffence().defensePoints
            + playersHelper.getWinnerOffense().offensePoints;
    double looserPoints =
        playersHelper.getLooserDeffence().defensePoints
            + playersHelper.getLooserOffense().offensePoints;
    double total = winnerPoints + looserPoints;
    double slope = slope(looserPoints, total);
    return (int) Math.round(slope * factor * BASE_POINTS);
  }

  private double sevenToZeroMultiplier(MatchEO match) {
    return match.blueScore != null
            && match.redScore != null
            && (match.blueScore == 0 || match.redScore == 0)
        ? 2
        : 1;
  }

  public static class PlayersHelper {
    public final PlayerEO blueDefense;
    public final PlayerEO blueOffense;
    public final PlayerEO redDefense;
    public final PlayerEO redOffense;
    public final MatchEO match;

    public boolean wonByBlue;

    public PlayersHelper(PlayerRepository playerRepository, MatchEO match) {
      this.match = match;
      this.wonByBlue =
          match.redScore != null && match.blueScore != null
              ? match.blueScore > match.redScore
              : true;
      blueDefense = playerRepository.findByIdOptional(match.playerBlueDefenseUuid).get();
      blueOffense = playerRepository.findByIdOptional(match.playerBlueOffenseUuid).get();
      redDefense = playerRepository.findByIdOptional(match.playerRedDefenseUuid).get();
      redOffense = playerRepository.findByIdOptional(match.playerRedOffenseUuid).get();
    }

    public PlayerEO getWinnerDeffence() {
      return wonByBlue ? blueDefense : redDefense;
    }

    public PlayerEO getWinnerOffense() {
      return wonByBlue ? blueOffense : redOffense;
    }

    public PlayerEO getLooserDeffence() {
      return !wonByBlue ? blueDefense : redDefense;
    }

    public PlayerEO getLooserOffense() {
      return !wonByBlue ? blueOffense : redOffense;
    }

    public List<PlayerEO> getAll() {
      return Arrays.asList(blueDefense, blueOffense, redDefense, redOffense);
    }
  }
}
