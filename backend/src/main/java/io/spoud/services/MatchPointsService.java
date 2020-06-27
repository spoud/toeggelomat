package io.spoud.services;

import io.spoud.data.definition.Match;
import io.spoud.data.entities.MatchEO;
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

  public MatchEO computePotentialPoints(MatchEO matchEO) {
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchEO);
    playersHelper.setWonByBlue(true);
    matchEO.setPotentialBluePoints(calcPoints(playersHelper));
    playersHelper.setWonByBlue(false);
    matchEO.setPotentialRedPoints(calcPoints(playersHelper));
    return matchEO;
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
      blueDefense =
          PlayerBO.from(playerRepository.findByUuid(match.getBlueDefense()).orElseThrow());
      blueOffense =
          PlayerBO.from(playerRepository.findByUuid(match.getBlueOffense()).orElseThrow());
      redDefense = PlayerBO.from(playerRepository.findByUuid(match.getRedDefense()).orElseThrow());
      redOffense = PlayerBO.from(playerRepository.findByUuid(match.getRedOffense()).orElseThrow());
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
