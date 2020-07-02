package io.spoud.services;

import io.spoud.data.definition.Match;
import io.spoud.data.entities.MatchProposition;
import io.spoud.data.kafka.MatchResultBO;
import io.spoud.data.kafka.PlayerBO;
import io.spoud.data.kafka.PointedMatchResultBO;
import io.spoud.repositories.PlayerRepository;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class MatchPointsService {

  public static final int ADDITIONAL_POINT_FOR_PLAYING = 1;

  @Inject private PlayerRepository playerRepository;

  public PointedMatchResultBO computePoints(MatchResultBO matchEO) {
    PointedMatchResultBO resultBO = PointedMatchResultBO.from(matchEO);
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchEO);

    resultBO.setWinnerDefense(playersHelper.getWinnerDefense());
    resultBO.setWinnerOffense(playersHelper.getWinnerOffense());
    resultBO.setLoserDefense(playersHelper.getLooserDefense());
    resultBO.setLoserOffense(playersHelper.getLooserOffense());

    int points = calcPoints(playersHelper);
    resultBO.setPoints(points);
    return resultBO;
  }

  public MatchProposition computePotentialPoints(MatchProposition matchProposition) {
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchProposition);
    playersHelper.setWonByBlue(true);
    matchProposition.setPotentialBluePoints(calcPoints(playersHelper));
    playersHelper.setWonByBlue(false);
    matchProposition.setPotentialRedPoints(calcPoints(playersHelper));
    return matchProposition;
  }

  private static final double LI_POINTS_SLOPE = 4;
  private static final double LI_POINTS_OFFSET = -1.5;

  private static final int BASE_POINTS = 40;

  private int calcPoints(PlayersHelper playersHelper) {
    double factor = 1.0;
    factor *= zeroMultiplier(playersHelper.getMatch());
    factor *= birthdayMultiplier(playersHelper.getWinnerOffense());
    factor *= birthdayMultiplier(playersHelper.getWinnerDefense());
    factor *= birthdayMultiplier(playersHelper.getLooserOffense());
    factor *= birthdayMultiplier(playersHelper.getLooserDefense());
    double winnerPoints =
        playersHelper.getWinnerDefense().getDefensePoints()
            + playersHelper.getWinnerOffense().getOffensePoints();
    double looserPoints =
        playersHelper.getLooserDefense().getDefensePoints()
            + playersHelper.getLooserOffense().getOffensePoints();
    double total = winnerPoints + looserPoints;
    double slope = slope(looserPoints, total);
    return (int) Math.round(slope * factor * BASE_POINTS);
  }

  private static double clamp(double d) {
    return Math.max(Math.min(d, 1.0), 0);
  }

  public static double slope(double looserPoints, double totalPoints) {
    return clamp(
        LI_POINTS_OFFSET + LI_POINTS_SLOPE * (double) (looserPoints) / (double) (totalPoints));
  }

  private double birthdayMultiplier(PlayerBO player) {
    return 1; // add birthdays
  }

  private double zeroMultiplier(Match match) {
    return match.getBlueScore() != null
            && match.getRedScore() != null
            && (match.getBlueScore() == 0 || match.getRedScore() == 0)
        ? 2
        : 1;
  }

  @Getter
  public static class PlayersHelper {
    private final Match match;
    private final PlayerBO blueOffense;
    private final PlayerBO blueDefense;
    private final PlayerBO redOffense;
    private final PlayerBO redDefense;

    @Setter private boolean wonByBlue;

    public PlayersHelper(PlayerRepository playerRepository, Match match) {
      this.match = match;
      blueDefense = playerRepository.findByUuid(match.getBlueDefense());
      blueOffense = playerRepository.findByUuid(match.getBlueOffense());
      redDefense = playerRepository.findByUuid(match.getRedDefense());
      redOffense = playerRepository.findByUuid(match.getRedOffense());
      this.wonByBlue =
          match.getRedScore() == null
              || match.getBlueScore() == null
              || match.getBlueScore() > match.getRedScore();
    }

    public PlayerBO getWinnerDefense() {
      return wonByBlue ? blueDefense : redOffense;
    }

    public PlayerBO getWinnerOffense() {
      return wonByBlue ? blueOffense : redOffense;
    }

    public PlayerBO getLooserDefense() {
      return !wonByBlue ? blueDefense : redDefense;
    }

    public PlayerBO getLooserOffense() {
      return !wonByBlue ? blueOffense : redOffense;
    }

    public List<PlayerBO> getAll() {
      return Arrays.asList(blueDefense, blueOffense, redDefense, redOffense);
    }
  }
}
